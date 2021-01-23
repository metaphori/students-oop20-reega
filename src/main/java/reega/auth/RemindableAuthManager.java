/**
 *
 */
package reega.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.inject.Inject;

import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.io.IOController;
import reega.logging.ExceptionHandler;
import reega.users.GenericUser;
import reega.users.NewUser;

/**
 * @author Marco
 *
 *         Authentication controller that uses a file token to authenticate
 *         without a password
 */
public class RemindableAuthManager implements AuthManager {

    /**
     * Data controller used for the login
     */
    private final AuthController authController;
    private final ExceptionHandler exceptionHandler;
    private final IOController ioController;

    @Inject
    public RemindableAuthManager(final AuthController authController, final ExceptionHandler exceptionHandler,
            final IOController ioController) {
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
        final Optional<UserAuth> uAuth = this.readUserAuthenticationFromDisk();
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
     * Generic login given a {@code userMethod} that is a string and a password
     * {@code pwd}
     *
     * @param userMethod       credential to login
     * @param pwd              password (not encrypted)
     * @param saveToken        true if needed to save the token
     * @param invocationMethod method to invoke for logging in
     * @return a filled in Optional if the login successfully returned, an empty
     *         Optional otherwise
     */
    private Optional<GenericUser> login(final String userMethod, final String pwd, final boolean saveToken,
            final BiFunction<String, String, Optional<GenericUser>> invocationMethod) {
        final Optional<GenericUser> loggedInUser = invocationMethod.apply(userMethod, pwd);

        if (saveToken) {
            // Save the token
            loggedInUser.ifPresent(usr -> {
                final UserAuth uAuth = new UserAuth(usr.getId());
                this.storeUserAuthentication(uAuth);
            });
        } else {
            // Delete the token if it exists
            loggedInUser.ifPresent(usr -> {
                if (this.getExistingTokenFile().isPresent()) {
                    this.deleteUserAuthentication(usr.getId());
                }
            });
        }

        return loggedInUser;
    }

    /**
     * Store the user authentication with the {@link #authController} and in the
     * disk
     *
     * @param userAuth user authentication to save
     * @see #storeUserAuthenticationToDisk(UserAuth)
     */
    private void storeUserAuthentication(final UserAuth userAuth) {
        try {
            this.authController.storeUserCredentials(userAuth.getUserID(), userAuth.getSelector(),
                    userAuth.getValidator());
        } catch (final SQLException | IOException e) {
            this.exceptionHandler.handleException(e);
            return;
        }
        this.storeUserAuthenticationToDisk(userAuth);
    }

    /**
     * Store the user authentication in the disk
     *
     * @param userAuth user authentication
     */
    private void storeUserAuthenticationToDisk(final UserAuth userAuth) {
        final Optional<File> tokenFileOptional = this.getExistingTokenFile();
        if (tokenFileOptional.isPresent()) {
            return;
        }

        final File tokenFile = this.getTokenFile();
        try {
            // Create the file if it doesn't exist
            tokenFile.createNewFile();
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "savePasswordAsToken -> Creating token file");
            return;
        }

        try (FileOutputStream stream = new FileOutputStream(tokenFile);
                ObjectOutputStream oos = new ObjectOutputStream(stream)) {
            oos.writeObject(userAuth);
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "readUserAuthentication -> Reading the token file");
        }
    }

    /**
     * Read the user authentication token from the disk
     *
     * @return an empty Optional if any operation failed or the file isn't in the
     *         correct format, a filled in Optional otherwise
     */
    private Optional<UserAuth> readUserAuthenticationFromDisk() {
        final Optional<File> tokenFileOptional = this.getExistingTokenFile();
        if (tokenFileOptional.isEmpty()) {
            return Optional.empty();
        }

        final File tokenFile = tokenFileOptional.get();

        final UserAuth userAuth;
        try (FileInputStream stream = new FileInputStream(tokenFile);
                ObjectInputStream oos = new ObjectInputStream(stream)) {
            userAuth = (UserAuth) oos.readObject();
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "readUserAuthentication -> Reading the token file IO");
            return Optional.empty();
        } catch (final ClassCastException | ClassNotFoundException e) {
            this.exceptionHandler.handleException(e, "readUserAuthentication -> Invalid format");
            return Optional.empty();
        }

        return Optional.of(userAuth);
    }

    /**
     * Delete user authentication from the disk
     *
     * @return true if the operation successfully ended, false otherwise
     */
    private boolean deleteUserAuthenticationFromDisk() {
        final Optional<File> tokenFileOptional = this.getExistingTokenFile();
        if (tokenFileOptional.isEmpty()) {
            return true;
        }

        final File tokenFile = tokenFileOptional.get();
        try {
            Files.delete(Path.of(tokenFile.getAbsolutePath()));
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "deleteUserAuthentication");
            return false;
        }
        return true;
    }

    /**
     * Delete the user authentication with the {@link #authController} and in the
     * disk
     *
     * @param userID ID of the user that needs to revoke its authentication
     * @return true if the operation successfully ended, false otherwise
     */
    private boolean deleteUserAuthentication(final int userID) {
        try {
            this.authController.userLogout(userID);
        } catch (final SQLException | IOException e) {
            this.exceptionHandler.handleException(e, "logout -> db logout");
            return false;
        }
        return this.deleteUserAuthenticationFromDisk();
    }

    /**
     * Get the token file if it exists, otherwise an empty Optional
     *
     * @return a filled in Optional with the token file if the token file exists, an
     *         empty Optional otherwise
     */
    private Optional<File> getExistingTokenFile() {
        return Optional.of(this.getTokenFile()).filter(File::exists);
    }

    /**
     * Get the token file
     *
     * @return the token file
     */
    private File getTokenFile() {
        return new File(this.ioController.getTokenFilePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logout(final int userID) {
        return this.deleteUserAuthentication(userID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logout(final GenericUser user) {
        return this.logout(user.getId());
    }

}
