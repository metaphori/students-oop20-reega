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
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.commons.lang3.SystemUtils;

import reega.data.AuthController;
import reega.data.models.UserAuth;
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

//    private static final Logger LOGGER = LoggerFactory.getLogger(RemindableAuthController.class);

    /**
     * URI of the directory of the app
     */
    private static final String APP_DIRECTORY_URI;
    /**
     * URI of the token file
     */
    private static final String TOKEN_FILE_URI;

    static {
        final String baseDir = System.getProperty("user.home");
        // For Windows, create a folder in the AppData\Local directory of the current
        // user, for the other systems create a folder in the /home/user directory
        if (SystemUtils.IS_OS_WINDOWS) {
            APP_DIRECTORY_URI = baseDir + File.separator + "AppData" + File.separator + "Local" + File.separator
                    + "Reega";
        } else {
            APP_DIRECTORY_URI = baseDir + File.separator + ".reega";
        }

        final File dir = new File(RemindableAuthManager.APP_DIRECTORY_URI);
        if (!dir.exists()) {
            // Create the directory if it doesn't exist
            dir.mkdir();
        }

        TOKEN_FILE_URI = RemindableAuthManager.APP_DIRECTORY_URI + File.separator + "token.reega";
    }

    /**
     * Data controller used for the login
     */
    private final AuthController authController;
    private final ExceptionHandler exceptionHandler;

    public RemindableAuthManager(final AuthController authController, final ExceptionHandler exceptionHandler) {
        Objects.requireNonNull(authController);
        Objects.requireNonNull(exceptionHandler);
        this.authController = authController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GenericUser> tryLoginWithoutPassword() {
        final Optional<UserAuth> uAuth = this.readUserAuthentication();
        if (uAuth.isEmpty()) {
            return Optional.empty();
        }
        final Optional<GenericUser> loggedInUser;
        try {
            loggedInUser = Optional.ofNullable(this.authController.tokenLogin(uAuth.get()));
        } catch (final SQLException e) {
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
        } catch (final SQLException e) {
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
            } catch (final SQLException e) {
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
            } catch (final SQLException e) {
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
            loggedInUser.ifPresent(usr -> {
                final UserAuth uAuth = new UserAuth(usr.getId());
                try {
                    this.authController.storeUserCredentials(uAuth.getUserID(), uAuth.getSelector(),
                            uAuth.getValidator());
                } catch (final SQLException | IOException e) {
                    this.exceptionHandler.handleException(e);
                }
                this.storeUserAuthentication(uAuth);
            });
        }

        return loggedInUser;
    }

    /**
     * Store the user authentication
     *
     * @param userAuth user authentication
     */
    private void storeUserAuthentication(final UserAuth userAuth) {
        final File tokenFile = new File(RemindableAuthManager.TOKEN_FILE_URI);
        if (!tokenFile.exists()) {
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
                return;
            }
        }
    }

    /**
     * Read the user authentication token from the disk
     *
     * @return an empty Optional if any operation failed or the file isn't in the
     *         correct format, a filled in Optional otherwise
     */
    private Optional<UserAuth> readUserAuthentication() {
        final File tokenFile = new File(RemindableAuthManager.TOKEN_FILE_URI);
        if (!tokenFile.exists()) {
            return Optional.empty();
        }
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

}
