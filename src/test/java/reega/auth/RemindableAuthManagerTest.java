/**
 *
 */
package reega.auth;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import reega.data.factory.AuthControllerFactory;
import reega.data.factory.UserControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.remote.RemoteConnection;
import reega.io.IOControllerFactory;
import reega.io.MockIOController;
import reega.io.TokenIOController;
import reega.users.NewUser;
import reega.users.Role;
import reega.users.User;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Marco
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
class RemindableAuthManagerTest {

    private RemoteConnection connection;
    private TokenIOController ioController;
    private AuthManager authManager;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        ioController = IOControllerFactory.createTokenIOController(new MockIOController());
        authManager = new RemindableAuthManager(
                AuthControllerFactory.getRemoteAuthController(connection),
                UserControllerFactory.getRemoteUserController(connection),
                new MockExceptionHandler(), ioController);
        authManager.logout();
        connection.logout();
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @AfterEach
    void cleanUsers() {
        // Delete the token file if it exists
        final File tokenFile = new File(this.ioController.getTokenFilePath());
        if (tokenFile.exists()) {
            tokenFile.delete();
        }
    }

    @Test
    public void addUserAndLoginTest() {
        // Try to login with email or fiscal code, without having created credentials
        Optional<User> u = this.authManager.emailLogin("test@reega.it", "PASSWORD", false);
        Assertions.assertTrue(u.isEmpty());
        u = this.authManager.fiscalCodeLogin("ZZZ999", "PASSWORD", false);
        Assertions.assertTrue(u.isEmpty());

        // Create a new user and try an email login
        NewUser newUser = new NewUser(Role.USER, "test", "surname", "addusertest1@reega.it", "AUT1", "PASSWORD");
        this.authManager.createUser(newUser);
        u = this.authManager.emailLogin("addusertest1@reega.it", "PASSWORD", false);
        Assertions.assertTrue(u.isPresent());
        connection.logout();

        // Create a new user and try a fiscal code login
        newUser = new NewUser(Role.USER, "test", "surname", "addusertest2@reega.it", "AUT2", "PASSWORD");
        this.authManager.createUser(newUser);
        u = this.authManager.fiscalCodeLogin("AUT2", "PASSWORD", false);
        Assertions.assertTrue(u.isPresent());
        connection.logout();
    }

    @Test
    public void tokenLoginTest() {
        // Create two users
        final NewUser newUser1 = new NewUser(Role.USER, "test1", "surname1", "tokentest1@reega.it", "TKL1", "PASSWORD");
        final NewUser newUser2 = new NewUser(Role.USER, "test1", "surname2", "tokentest2@reega.it", "TKL2", "PASSWORD");
        this.authManager.createUser(newUser1);
        this.authManager.createUser(newUser2);

        // Login the second user and save the token
        this.authManager.emailLogin("tokentest2@reega.it", "PASSWORD",
                true);

        this.connection.logout();

        // Try the login without password
        Optional<User> usr = this.authManager.tryLoginWithoutPassword();
        Assertions.assertTrue(usr.isPresent());
        Assertions.assertEquals(newUser2.getEmail(), usr.get().getEmail());
        this.authManager.logout();
        this.connection.logout();
    }

    @Test
    public void logoutTest() {
        // Create a new user
        final NewUser newUser = new NewUser(Role.USER, "test", "surname", "logouttest@reega.it", "LT1", "PASSWORD");
        this.authManager.createUser(newUser);
        // Login and store the token
        this.authManager.emailLogin("logouttest@reega.it", "PASSWORD", true);
        this.connection.logout();
        Optional<User> user = this.authManager.tryLoginWithoutPassword();
        Assertions.assertTrue(user.isPresent());

        // Logout (so that if a token exists, it gets deleted)
        this.authManager.logout();
        this.connection.logout();

        // Try the login without password
        user = this.authManager.tryLoginWithoutPassword();
        Assertions.assertTrue(user.isEmpty());

        // Try the login with email
        user = this.authManager.emailLogin("logouttest@reega.it", "PASSWORD", false);
        Assertions.assertTrue(user.isPresent());
        this.connection.logout();
    }

}
