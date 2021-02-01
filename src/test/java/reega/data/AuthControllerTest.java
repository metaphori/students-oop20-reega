package reega.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import okhttp3.mockwebserver.Dispatcher;
import reega.data.mock.MockConnection;
import reega.data.mock.MockedAuthService;
import reega.data.mock.RequestDispatcher;
import reega.data.models.UserAuth;
import reega.data.remote.RemoteAuthAPI;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {
    private static MockConnection connection;
    private static RemoteAuthAPI authAPI;
    private static MockedAuthService authService;

    @BeforeAll
    static void setup() throws IOException {
        authService = new MockedAuthService();
        final Dispatcher dispatcher = new RequestDispatcher(null, authService);
        connection = new MockConnection(dispatcher);
        authAPI = connection.getAuthAPI();
    }

    @AfterAll
    static void cleanup() throws IOException {
        connection.close();
    }

    @AfterEach
    void cleanUsers() {
        authService.clean();
    }

    @Test
    public void addUserAndLoginTest() throws IOException {
        GenericUser u = authAPI.emailLogin("test@reega.it", "PASSWORD");
        assertNull(u);
        u = authAPI.fiscalCodeLogin("ZZZ999", "PASSWORD");
        assertNull(u);

        NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "ABC123", "PASSWORD");
        authAPI.addUser(newUser);
        u = authAPI.emailLogin("test@reega.it", "PASSWORD");
        assertNotNull(u);

        newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "ZZZ999", "PASSWORD");
        authAPI.addUser(newUser);
        u = authAPI.fiscalCodeLogin("ZZZ999", "PASSWORD");
        assertNotNull(u);
    }

    @Test
    public void tokenLoginTest() throws IOException {
        final NewUser newUser1 = new NewUser(Role.USER, "test1", "surname1", "test1@reega.it", "ABC123", "PASSWORD");
        final NewUser newUser2 = new NewUser(Role.USER, "test1", "surname2", "test2@reega.it", "ZZZ999", "PASSWORD");
        authService.setUserID(1);
        authAPI.addUser(newUser1);

        authService.setUserID(2);
        authAPI.addUser(newUser2);

        // store token for the current user (id 2)
        final UserAuth auth = new UserAuth();
        authAPI.storeUserCredentials(auth.getSelector(), auth.getValidator());

        final GenericUser user = authAPI.tokenLogin(auth);
        assertNotNull(user);
        assertEquals(newUser2.getEmail(), user.getEmail());
    }

    @Test
    public void logoutTest() throws IOException {
        // simulate login with remember-me
        final NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "ABC123", "PASSWORD");
        authAPI.addUser(newUser);
        final UserAuth auth = new UserAuth();
        authAPI.storeUserCredentials(auth.getSelector(), auth.getValidator());

        GenericUser user = authAPI.tokenLogin(auth);
        assertNotNull(user);

        // it uses the default user id
        authAPI.userLogout();

        user = authAPI.tokenLogin(auth);
        assertNull(user);

        user = authAPI.emailLogin("test@reega.it", "PASSWORD");
        assertNotNull(user);
    }
}
