/**
 *
 */
package reega.auth;

import okhttp3.mockwebserver.Dispatcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import reega.io.IOControllerFactory;
import reega.io.MockIOController;
import reega.io.TokenIOController;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Marco
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
class RemindableAuthManagerTest {

    private static MockConnection connection;
    private static MockedAuthService authService;
    private static TokenIOController ioController;
    private static AuthManager authManager;

    @BeforeAll
    static void setup() throws IOException {
        RemindableAuthManagerTest.authService = new MockedAuthService();
        final Dispatcher dispatcher = new RequestDispatcher(null, RemindableAuthManagerTest.authService);
        RemindableAuthManagerTest.connection = new MockConnection(dispatcher);
        RemindableAuthManagerTest.ioController = IOControllerFactory.createTokenIOController(new MockIOController());
        RemindableAuthManagerTest.authManager = new RemindableAuthManager(
                RemindableAuthManagerTest.connection.getAuthAPI(), new MockExceptionHandler(),
                RemindableAuthManagerTest.ioController);
    }

    @AfterAll
    static void cleanup() throws IOException {
        RemindableAuthManagerTest.connection.close();

    }

    @AfterEach
    void cleanUsers() {
        RemindableAuthManagerTest.authService.clean();
        // Delete the token file if it exists
        final File tokenFile = new File(RemindableAuthManagerTest.ioController.getTokenFilePath());
        if (tokenFile.exists()) {
            tokenFile.delete();
        }
    }

    @Test
    public void addUserAndLoginTest() {
        // Try to login with email or fiscal code, without having created credentials
        Optional<GenericUser> u = RemindableAuthManagerTest.authManager.emailLogin("test@reega.it", "PASSWORD", false);
        Assertions.assertTrue(u.isEmpty());
        u = RemindableAuthManagerTest.authManager.fiscalCodeLogin("ZZZ999", "PASSWORD", false);
        Assertions.assertTrue(u.isEmpty());

        // Create a new user and try an email login
        NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "ABC123", "PASSWORD");
        RemindableAuthManagerTest.authManager.createUser(newUser);
        u = RemindableAuthManagerTest.authManager.emailLogin("test@reega.it", "PASSWORD", false);
        Assertions.assertTrue(u.isPresent());

        // Create a new user and try a fiscal code login
        newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "ZZZ999", "PASSWORD");
        RemindableAuthManagerTest.authManager.createUser(newUser);
        u = RemindableAuthManagerTest.authManager.fiscalCodeLogin("ZZZ999", "PASSWORD", false);
        Assertions.assertTrue(u.isPresent());
    }

    @Test
    public void tokenLoginTest() {
        // Create two users
        final NewUser newUser1 = new NewUser(Role.USER, "test1", "surname1", "test1@reega.it", "ABC123", "PASSWORD");
        final NewUser newUser2 = new NewUser(Role.USER, "test1", "surname2", "test2@reega.it", "ZZZ999", "PASSWORD");
        // Set the user IDs
        RemindableAuthManagerTest.authService.setUserID(1);
        RemindableAuthManagerTest.authManager.createUser(newUser1);

        RemindableAuthManagerTest.authService.setUserID(2);
        RemindableAuthManagerTest.authManager.createUser(newUser2);

        // Login the second user and save the token
        Optional<GenericUser> usr = RemindableAuthManagerTest.authManager.emailLogin("test2@reega.it", "PASSWORD",
                true);

        // Try the login without password
        usr = RemindableAuthManagerTest.authManager.tryLoginWithoutPassword();
        Assertions.assertTrue(usr.isPresent());
        Assertions.assertEquals(newUser2.getEmail(), usr.get().getEmail());
    }

    @Test
    public void logoutTest() {
        // Create a new user
        final NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "ABC123", "PASSWORD");
        RemindableAuthManagerTest.authManager.createUser(newUser);
        // Login and store the token
        RemindableAuthManagerTest.authManager.emailLogin("test@reega.it", "PASSWORD", true);

        Optional<GenericUser> user = RemindableAuthManagerTest.authManager.tryLoginWithoutPassword();
        Assertions.assertTrue(user.isPresent());

        // Logout (so that if a token exists, it gets deleted)
        RemindableAuthManagerTest.authManager.logout();

        // Try the login without password
        user = RemindableAuthManagerTest.authManager.tryLoginWithoutPassword();
        Assertions.assertTrue(user.isEmpty());

        // Try the login with email
        user = RemindableAuthManagerTest.authManager.emailLogin("test@reega.it", "PASSWORD", false);
        Assertions.assertTrue(user.isPresent());
    }

}
