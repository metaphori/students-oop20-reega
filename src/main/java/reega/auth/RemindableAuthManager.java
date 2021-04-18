/**
 *
 */
package reega.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.AuthController;
import reega.data.UserController;
import reega.data.models.UserAuth;
import reega.io.TokenIOController;
import reega.logging.ExceptionHandler;
import reega.logging.SimpleExceptionHandler;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.User;
import reega.util.ValueResult;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Marco
 *
 *         Authentication controller that uses a file token to authenticate without a password
 */
public class RemindableAuthManager implements AuthManager {

    /**
     * Data controller used for the login
     */
    private final AuthController authController;
    private final UserController userController;
    private final TokenIOController ioController;
    private final ExceptionHandler exceptionHandler;

    @Inject
    public RemindableAuthManager(final AuthController authController, final UserController userController,final TokenIOController ioController, final ExceptionHandler exceptionHandler) {
        this.authController = authController;
        this.userController = userController;
        this.ioController = ioController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ValueResult<Optional<User>> tryLoginWithoutPassword() {
        Optional<UserAuth> uAuth;
        try {
            uAuth = this.ioController.readUserAuthentication();
        } catch (final IOException e) {
            String errString = "Error when trying to read the user authentication from the disk";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(Optional.empty(), errString);
        }
        if (uAuth.isEmpty()) {
            return new ValueResult<>(Optional.empty());
        }

        Optional<User> loggedInUser;
        try {
            loggedInUser = Optional.ofNullable(this.authController.tokenLogin(uAuth.get()));
        } catch (final IOException e) {
            String errString = "Error when trying to read the user authentication from the database";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(Optional.empty(), errString);
        }

        if (loggedInUser.isEmpty()) {
            // If the authentication is not correct, then delete the authentication
            this.deleteUserAuthenticationFromDisk();
        }

        return new ValueResult<>(loggedInUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Void> createUser(final NewUser user) {
        try {
            this.userController.addUser(user);
        } catch (final IOException e) {
            String errString = "Error when trying to add a new user";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }
        return new ValueResult<>((Void)null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Optional<User>> emailLogin(final String email, final String pwd, final boolean saveToken) {
        return this.login(email, pwd, saveToken, (userMethod, hash) -> {
            Optional<User> loggedInUser;
            try {
                loggedInUser = Optional.ofNullable(this.authController.emailLogin(userMethod, hash));
            } catch (final IOException e) {
                String errString = "Failed to login into the application with the email";
                this.exceptionHandler.handleException(e, errString);
                return new ValueResult<>(Optional.empty(), errString);
            }
            return new ValueResult<>(loggedInUser);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Optional<User>> fiscalCodeLogin(final String fiscalCode, final String pwd, final boolean saveToken) {
        return this.login(fiscalCode, pwd, saveToken, (userMethod, hash) -> {
            Optional<User> loggedInUser;
            try {
                loggedInUser = Optional.ofNullable(this.authController.fiscalCodeLogin(userMethod, hash));
            } catch (final IOException e) {
                String errString = "Failed to login into the application with the email";
                this.exceptionHandler.handleException(e, errString);
                return new ValueResult<>(Optional.empty(), errString);
            }
            return new ValueResult<>(loggedInUser);
        });
    }

    /**
     * Generic login given a {@code userMethod} that is a string and a password {@code pwd}
     *
     * @param userMethod       credential to login
     * @param pwd              password (not encrypted)
     * @param saveToken        true if needed to save the token
     * @param invocationMethod method to invoke for logging in
     * @return a filled in Optional if the login successfully returned, an empty Optional otherwise
     */
    private ValueResult<Optional<User>> login(final String userMethod, final String pwd, final boolean saveToken,
                                 final BiFunction<String, String, ValueResult<Optional<User>>> invocationMethod) {
        final ValueResult<Optional<User>> loggedInUser = invocationMethod.apply(userMethod, pwd);

        if (loggedInUser.isValid()) {
            Optional<User> currUser = loggedInUser.getValue();
            if (currUser.isPresent()) {
                if (saveToken) {
                    // Save the token
                    final UserAuth uAuth = new UserAuth();
                    return loggedInUser.merge(this.storeUserAuthentication(uAuth));
                }
                if (this.ioController.tokenFileExists()) {
                    return loggedInUser.merge(this.deleteUserAuthentication());
                }
            }
        }

        return loggedInUser;
    }

    /**
     * Store the user authentication with the {@link #authController} and in the disk
     *
     * @param userAuth user authentication to save
     */
    private ValueResult<Void> storeUserAuthentication(final UserAuth userAuth) {
        try {
            this.authController.storeUserCredentials(userAuth.getSelector(), userAuth.getValidator());
        } catch (final IOException e) {
            String errString = "Failed to store the user credentials in the database";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }

        try {
            this.ioController.storeUserAuthentication(userAuth);
        } catch (final IOException e) {
            String errString = "Failed to store the user credentials on the disk";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }
        return new ValueResult<>((Void)null);
    }

    /**
     * Delete the user authentication with the {@link #authController} and in the disk
     *
     * @return true if the operation successfully ended, false otherwise
     */
    private ValueResult<Void> deleteUserAuthentication() {
        try {
            this.authController.userLogout();
        } catch (final IOException e) {
            String errString = "Failed to logout from the db";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }

        return this.deleteUserAuthenticationFromDisk();
    }

    /**
     * Delete the user authentication from the disk
     *
     * @return true if the operation successfully ended, false otherwise
     */
    private ValueResult<Void> deleteUserAuthenticationFromDisk() {
        try {
            this.ioController.deleteUserAuthentication();
        } catch (final IOException e) {
            String errString = "Failed to delete the user authentication from the disk";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }
        return new ValueResult<>((Void)null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Void> logout() {
        return this.deleteUserAuthentication();
    }

}
