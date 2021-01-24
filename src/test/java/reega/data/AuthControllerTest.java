package reega.data;

import okhttp3.mockwebserver.Dispatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import reega.data.mock.MockConnection;
import reega.data.mock.MockedAuthService;
import reega.data.mock.RequestDispatcher;
import reega.data.remote.RemoteAuthAPI;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {
    private static MockConnection connection;
    private static RemoteAuthAPI authAPI;

    @BeforeAll
    static void setup() throws IOException {
        Dispatcher dispatcher = new RequestDispatcher(null, new MockedAuthService());
        connection = new MockConnection(dispatcher);
        authAPI = connection.getAuthAPI();
    }

    @AfterAll
    static void cleanup() throws IOException {
        connection.close();
    }
}
