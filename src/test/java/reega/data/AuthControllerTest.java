package reega.data;

import org.junit.jupiter.api.*;
import reega.data.mock.TestConnection;
import reega.data.models.UserAuth;
import reega.data.remote.RemoteConnection;
import reega.users.NewUser;
import reega.users.Role;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {
    private RemoteConnection connection;
    private AuthController authController;
    private UserController userController;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        authController = AuthControllerFactory.getRemoteAuthController(connection);
        userController = UserControllerFactory.getRemoteUserController(connection);

        authController.userLogout();
        connection.logout();
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void addUserAndLoginTest() throws IOException {
        // test null on user not present in DB
        var u = authController.emailLogin("not_present@reega.it", "AES_PASSWORD");
        assertNull(u);
        u = authController.fiscalCodeLogin("ZZZ999", "PASSWORD");
        assertNull(u);

        // add user as not autenticated
        NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "TTT111", "PASSWORD");
        userController.addUser(newUser);
        var user = authController.emailLogin("test@reega.it", "PASSWORD");
        assertNotNull(user);
        assertEquals("test", user.getName());
        assertEquals("surname", user.getSurname());
        assertEquals("test@reega.it", user.getEmail());
        assertEquals(Role.USER, user.getRole());

        connection.logout();

        newUser = new NewUser(Role.USER, "test", "surname", "test2@reega.it", "ZZZ999", "PASSWORD");
        userController.addUser(newUser);
        user = authController.fiscalCodeLogin("ZZZ999", "PASSWORD");
        assertNotNull(user);
        assertEquals("test", user.getName());
        assertEquals("surname", user.getSurname());
        assertEquals("test2@reega.it", user.getEmail());
        assertEquals(Role.USER, user.getRole());

        connection.logout();
    }

    @Test
    @Order(2)
    public void removeUserTest() throws IOException {
        authController.emailLogin("admin@reega.it", "AES_PASSWORD");

        userController.removeUser("ZZZ999");
        var user = authController.fiscalCodeLogin("ZZZ999", "PASSWORD");
        assertNull(user);

        connection.logout();
    }

    @Test
    @Order(3)
    public void removeUserWithToken() throws IOException {
        // create new user and store "remind-me" token
        NewUser newUser = new NewUser(Role.USER, "name", "surname", "remind@reega.it", "BBB222", "PASSWORD");
        userController.addUser(newUser);

        var u = authController.emailLogin("remind@reega.it", "PASSWORD");
        assertNotNull(u);

        // store token for the default user (0)
        UserAuth auth = new UserAuth();
        authController.storeUserCredentials(auth.getSelector(), auth.getValidator());

        connection.logout();
        u = authController.emailLogin("admin@reega.it", "AES_PASSWORD");
        assertNotNull(u);

        userController.removeUser("BBB222");
        connection.logout();

        u = authController.fiscalCodeLogin("BBB222", "PASSWORD");
        assertNull(u);

        u = authController.tokenLogin(auth);
        assertNull(u);

        connection.logout();
    }

    @Test
    @Order(4)
    public void tokenLoginTest() throws IOException {
        final NewUser newUser = new NewUser(Role.USER, "test", "surname", "token_login@reega.it", "CCC333", "PASSWORD");
        userController.addUser(newUser);
        var user = authController.emailLogin("token_login@reega.it", "PASSWORD");
        assertNotNull(user);
        assertEquals("token_login@reega.it", user.getEmail());

        final UserAuth auth = new UserAuth("abcd", "efgh");
        authController.storeUserCredentials(auth.getSelector(), auth.getValidator());

        connection.logout();

        user = authController.tokenLogin(auth);
        assertNotNull(user);
        assertEquals("token_login@reega.it", user.getEmail());
        connection.logout();
    }

    @Test
    @Order(5)
    public void logoutTest() throws IOException {
        // simulate login with remember-me
        UserAuth auth = new UserAuth("abcd", "efgh");
        var user = authController.tokenLogin(auth);
        assertNotNull(user);
        assertEquals("token_login@reega.it", user.getEmail());

        authController.userLogout();
        connection.logout();

        user = authController.tokenLogin(auth);
        assertNull(user);

        user = authController.emailLogin("token_login@reega.it", "PASSWORD");
        assertNotNull(user);
    }
}
