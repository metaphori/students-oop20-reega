/**
 *
 */
package reega.auth;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import reega.io.IOController;
import reega.io.IOControllerFactory;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

/**
 * @author Marco
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
class RemindableAuthManagerTest {

    private final IOController ioController = IOControllerFactory.getDefaultIOController();
    private final AuthManager authController = new RemindableAuthManager(new MockAuthController(),
            new MockExceptionHandler(), this.ioController);

    @BeforeAll
    public void setupBeforeAll() {
    }

    @AfterEach
    public void setupAfterEach() {
        // Delete the token file if it exists
        final File tokenFile = new File(this.ioController.getTokenFilePath());
        if (tokenFile.exists()) {
            tokenFile.delete();
        }
    }

    @Test
    void emailLogin() {
        final Optional<GenericUser> loggedInUser = this.authController.emailLogin("Email", "Password", true);
        if (loggedInUser.isEmpty()) {
            Assertions.fail();
        }
        final Optional<GenericUser> notLoggedInUser = this.authController.emailLogin("Email",
                MockAuthController.HASH_SQL_EXCEPTION, true);
        if (notLoggedInUser.isPresent()) {
            Assertions.fail();
        }
    }

    @Test
    void fiscalCodeLogin() {
        final Optional<GenericUser> loggedInUser = this.authController.fiscalCodeLogin("Fiscal Code", "Password", true);
        if (loggedInUser.isEmpty()) {
            Assertions.fail();
        }
        final Optional<GenericUser> notLoggedInUser = this.authController.fiscalCodeLogin("Email",
                MockAuthController.HASH_SQL_EXCEPTION, true);
        if (notLoggedInUser.isPresent()) {
            Assertions.fail();
        }
    }

    @Test
    void tokenLogin() {
        final Optional<GenericUser> loggedInUser = this.authController.tryLoginWithoutPassword();
        if (loggedInUser.isPresent()) {
            Assertions.fail();
        }

        final Optional<GenericUser> saveableTokenUser = this.authController.fiscalCodeLogin("Fiscal Code", "Password",
                true);
        if (saveableTokenUser.isEmpty()) {
            Assertions.fail();
        }

        final Optional<GenericUser> loggedInUserWithoutPassword = this.authController.tryLoginWithoutPassword();
        if (loggedInUserWithoutPassword.isEmpty()) {
            Assertions.fail();
        }
    }

    @Test
    void createUser() {
        if (!this.authController.createUser(new NewUser(Role.ADMIN, "Name", "Surname", "EMAIL", "FiscalCode", "PWD"))) {
            Assertions.fail();
        }

        if (this.authController.createUser(new NewUser(Role.ADMIN, "Name", "Surname",
                MockAuthController.EMAIL_FOR_SQL_EXCEPTION, "FiscalCode", "PWD"))) {
            Assertions.fail();
        }
    }

}
