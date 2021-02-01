/**
 *
 */
package reega.auth;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.inject.Inject;

import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.io.TokenIOController;
import reega.logging.ExceptionHandler;
import reega.users.GenericUser;
import reega.users.NewUser;

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
    private final ExceptionHandler exceptionHandler;
    private final TokenIOController ioController;

    @Inject
    public RemindableAuthManager(final AuthController authController, final ExceptionHandler exceptionHandler,
            final TokenIOController ioController) {
        Objects.requireNonNull(authController);
        Objects.requireNonNull(exceptionHandler);
        Objects.requireNonNull(ioController);
        this.authController = authController;
        this.exceptionHandler = exceptionHandler;
        this.ioController = ioController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GenericUser> tryLoginWithoutPassword() {
        Optional<UserAuth> uAuth = Optional.empty();
        try {
            uAuth = this.ioController.readUserAuthentication();
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e);
        }
        if (uAuth.isEmpty()) {
            return Optional.empty();
        }

        final Optional<GenericUser> loggedInUser;
        try {
            loggedInUser = Optional.ofNullable(this.authController.tokenLogin(uAuth.get()));
        } catch (final SQLException | IOException e) {
            this.exceptionHandler.handleException(e);
            return Optional.empty();
        }

        return loggedInUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createUser(final NewUser user) {
        try {
            this.authController.addUser(user);
        } catch (final SQLException | IOException e) {
            this.exceptionHandler.handleException(e, "createUser");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GenericUser> emailLogin(final String email, final String pwd, final boolean saveToken) {
        return this.login(email, pwd, saveToken, (userMethod, hash) -> {
            Optional<GenericUser> loggedInUser;
            try {
                loggedInUser = Optional.ofNullable(this.authController.emailLogin(userMethod, hash));
            } catch (final SQLException | IOException e) {
                this.exceptionHandler.handleException(e, "emailLogin -> Login call");
                return Optional.empty();
            }
            return loggedInUser;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GenericUser> fiscalCodeLogin(final String fiscalCode, final String pwd, final boolean saveToken) {
        return this.login(fiscalCode, pwd, saveToken, (userMethod, hash) -> {
            Optional<GenericUser> loggedInUser;
            try {
                loggedInUser = Optional.ofNullable(this.authController.fiscalCodeLogin(userMethod, hash));
            } catch (final SQLException | IOException e) {
                this.exceptionHandler.handleException(e, "fiscalCodeLogin -> Login call");
                return Optional.empty();
            }
            return loggedInUser;
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
    private Optional<GenericUser> login(final String userMethod, final String pwd, final boolean saveToken,
            final BiFunction<String, String, Optional<GenericUser>> invocationMethod) {
        final Optional<GenericUser> loggedInUser = invocationMethod.apply(userMethod, pwd);

        if (saveToken) {
            // Save the token
            loggedInUser.ifPresent(usr -> {
                final UserAuth uAuth = new UserAuth();
                this.storeUserAuthentication(uAuth);
            });
        } else {
            // Delete the token if it exists
            loggedInUser.ifPresent(usr -> {
                if (this.ioController.tokenFileExists()) {
                    this.deleteUserAuthentication();
                }
            });
        }

        return loggedInUser;
    }

    /**
     * Store the user authentication with the {@link #authController} and in the disk
     *
     * @param userAuth user authentication to save
     */
    private void storeUserAuthentication(final UserAuth userAuth) {
        try {
            this.authController.storeUserCredentials(userAuth.getSelector(), userAuth.getValidator());
        } catch (final SQLException | IOException e) {
            this.exceptionHandler.handleException(e);
            return;
        }

        try {
            this.ioController.storeUserAuthentication(userAuth);
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e,
                    "storeUserAuthentication -> storing the authentication token to disk");
        }
    }

    /**
     * Delete the user authentication with the {@link #authController} and in the disk
     *
     * @return true if the operation successfully ended, false otherwise
     */
    private boolean deleteUserAuthentication() {
        try {
            this.authController.userLogout();
        } catch (final SQLException | IOException e) {
            this.exceptionHandler.handleException(e, "logout -> db logout");
            return false;
        }

        try {
            this.ioController.deleteUserAuthentication();
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e,
                    "deleteUserAuthentication -> deleting the authentication token from disk");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logout() {
        return this.deleteUserAuthentication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logout(final GenericUser user) {
        return this.logout();
    }

}
