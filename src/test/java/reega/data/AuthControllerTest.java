package reega.data;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import reega.data.factory.AuthControllerFactory;
import reega.data.factory.UserControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.UserAuth;
import reega.data.remote.RemoteConnection;
import reega.users.NewUser;
import reega.users.Role;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class AuthControllerTest {
    private RemoteConnection connection;
    private AuthController authController;
    private UserController userController;

    @BeforeAll
    public void setup() throws IOException {
        this.connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        this.authController = AuthControllerFactory.getRemoteAuthController(this.connection);
        this.userController = UserControllerFactory.getRemoteUserController(this.connection);

        this.authController.userLogout();
        this.connection.logout();
    }

    @AfterAll
    public void cleanup() throws IOException {
        this.connection.getService().terminateTest().execute();
        this.connection.logout();
    }

    @Test
    @Order(1)
    public void addUserAndLoginTest() throws IOException {
        // test null on user not present in DB
        var u = this.authController.emailLogin("not_present@reega.it", "AES_PASSWORD");
        Assertions.assertNull(u);
        u = this.authController.fiscalCodeLogin("ZZZ999", "PASSWORD");
        Assertions.assertNull(u);

        // add user as not autenticated
        NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "TTT111", "PASSWORD");
        this.userController.addUser(newUser);
        var user = this.authController.emailLogin("test@reega.it", "PASSWORD");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("test", user.getName());
        Assertions.assertEquals("surname", user.getSurname());
        Assertions.assertEquals("test@reega.it", user.getEmail());
        Assertions.assertEquals(Role.USER, user.getRole());

        this.connection.logout();

        newUser = new NewUser(Role.USER, "test", "surname", "test2@reega.it", "ZZZ999", "PASSWORD");
        this.userController.addUser(newUser);
        user = this.authController.fiscalCodeLogin("ZZZ999", "PASSWORD");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("test", user.getName());
        Assertions.assertEquals("surname", user.getSurname());
        Assertions.assertEquals("test2@reega.it", user.getEmail());
        Assertions.assertEquals(Role.USER, user.getRole());

        this.connection.logout();
    }

    @Test
    @Order(2)
    public void removeUserTest() throws IOException {
        this.authController.emailLogin("admin@reega.it", "AES_PASSWORD");

        this.userController.removeUser("ZZZ999");
        final var user = this.authController.fiscalCodeLogin("ZZZ999", "PASSWORD");
        Assertions.assertNull(user);

        this.connection.logout();
    }

    @Test
    @Order(3)
    public void removeUserWithToken() throws IOException {
        // create new user and store "remind-me" token
        final NewUser newUser = new NewUser(Role.USER, "name", "surname", "remind@reega.it", "BBB222", "PASSWORD");
        this.userController.addUser(newUser);

        var u = this.authController.emailLogin("remind@reega.it", "PASSWORD");
        Assertions.assertNotNull(u);

        // store token for the default user (0)
        final UserAuth auth = new UserAuth();
        this.authController.storeUserCredentials(auth.getSelector(), auth.getValidator());

        this.connection.logout();
        u = this.authController.emailLogin("admin@reega.it", "AES_PASSWORD");
        Assertions.assertNotNull(u);

        this.userController.removeUser("BBB222");
        this.connection.logout();

        u = this.authController.fiscalCodeLogin("BBB222", "PASSWORD");
        Assertions.assertNull(u);

        u = this.authController.tokenLogin(auth);
        Assertions.assertNull(u);

        this.connection.logout();
    }

    @Test
    @Order(4)
    public void tokenLoginTest() throws IOException {
        final NewUser newUser = new NewUser(Role.USER, "test", "surname", "token_login@reega.it", "CCC333", "PASSWORD");
        this.userController.addUser(newUser);
        var user = this.authController.emailLogin("token_login@reega.it", "PASSWORD");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("token_login@reega.it", user.getEmail());

        final UserAuth auth = new UserAuth("abcd", "efgh");
        this.authController.storeUserCredentials(auth.getSelector(), auth.getValidator());

        this.connection.logout();

        user = this.authController.tokenLogin(auth);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("token_login@reega.it", user.getEmail());
        this.connection.logout();
    }

    @Test
    @Order(5)
    public void logoutTest() throws IOException {
        // simulate login with remember-me
        final UserAuth auth = new UserAuth("abcd", "efgh");
        var user = this.authController.tokenLogin(auth);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("token_login@reega.it", user.getEmail());

        this.authController.userLogout();
        this.connection.logout();

        user = this.authController.tokenLogin(auth);
        Assertions.assertNull(user);

        user = this.authController.emailLogin("token_login@reega.it", "PASSWORD");
        Assertions.assertNotNull(user);
    }
}
