/**
 *
 */
package reega.auth;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import reega.data.DataController;
import reega.logging.ExceptionHandler;
import reega.users.GenericUser;
import reega.users.NewUser;

/**
 * @author Marco
 *
 *         Authentication controller that uses a file token to authenticate
 *         without a password
 */
public class RemindableAuthController implements AuthenticationController {

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

        final File dir = new File(RemindableAuthController.APP_DIRECTORY_URI);
        if (!dir.exists()) {
            // Create the directory if it doesn't exist
            dir.mkdir();
        }

        TOKEN_FILE_URI = RemindableAuthController.APP_DIRECTORY_URI + File.separator + "token.reega";
    }

    /**
     * Data controller used for the login
     */
    private final DataController dataController;
    private final ExceptionHandler exceptionHandler;

    public RemindableAuthController(final DataController dataController, final ExceptionHandler exceptionHandler) {
        Objects.requireNonNull(dataController);
        Objects.requireNonNull(exceptionHandler);
        this.dataController = dataController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GenericUser> tryLoginWithoutPassword() {
        final File tokenFile = new File(RemindableAuthController.TOKEN_FILE_URI);
        if (!tokenFile.exists()) {
            // If the token file doesn't exist, then there's no token for authentication
            return Optional.empty();
        }

        final String hash;

        try {
            hash = FileUtils.readFileToString(tokenFile, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "tryLoginWithoutPassword");
            return Optional.empty();
        }

        return this.login(hash);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createUser(final NewUser user) {
        try {
            this.dataController.addUser(user);
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
                loggedInUser = Optional.ofNullable(this.dataController.emailLogin(userMethod, hash));
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
                loggedInUser = Optional.ofNullable(this.dataController.fiscalCodeLogin(userMethod, hash));
            } catch (final SQLException e) {
                this.exceptionHandler.handleException(e, "fiscalCodeLogin -> Login call");
                return Optional.empty();
            }
            return loggedInUser;
        });
    }

    /**
     * Login into the app with only the hash
     *
     * @param hash password hash
     * @return an empty Optional if the login didn't succeed, filled in with the
     *         logged in user otherwise
     */
    private Optional<GenericUser> login(final String hash) {
        // TODO: Login with only an hash
        // dataController.hashLogin(hash);
        return null;
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
//        final Optional<String> optionalHash = this.encryptPassword(pwd);
//        if (optionalHash.isEmpty()) {
//            return Optional.empty();
//        }
//        final String hash = optionalHash.get();
        final Optional<GenericUser> loggedInUser = invocationMethod.apply(userMethod, pwd);

//        if (saveToken) {
//            loggedInUser.ifPresent(usr -> {
//                this.savePasswordAsToken(hash);
//            });
//        }

        return loggedInUser;
    }

    /**
     * Save the password as a token to disk: TODO
     *
     * @param hash hashed password
     */
//    private void savePasswordAsToken(final String hash) {
//        final File tokenFile = new File(RemindableAuthController.TOKEN_FILE_URI);
//        if (!tokenFile.exists()) {
//            try {
//                tokenFile.createNewFile();
//            } catch (final IOException e) {
//                this.exceptionHandler.handleException(e, "savePasswordAsToken -> Creating token file");
//                return;
//            }
//
//            try {
//                FileUtils.write(tokenFile, hash, StandardCharsets.UTF_8);
//            } catch (final IOException e) {
//                this.exceptionHandler.handleException(e, "savePasswordAsToken -> Saving the password");
//                return;
//            }
//        }
//    }

    /**
     * Encrypt {@code pwd} TODO
     *
     * @param pwd password to encrypt
     */
//    private Optional<String> encryptPassword(final String pwd) {
//        Objects.requireNonNull(pwd);
//        String hash;
//        try {
//            hash = AES.encrypt(pwd.toCharArray());
//        } catch (final Exception e) {
//            this.exceptionHandler.handleException(e, "emailLogin -> AES");
//            return Optional.empty();
//        }
//        return Optional.ofNullable(hash);
//    }

}
