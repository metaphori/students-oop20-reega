package reega.users;

import org.junit.jupiter.api.*;
import reega.data.ContractController;
import reega.data.factory.ContractControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.remote.RemoteConnection;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSearch {
    private RemoteConnection connection;
    private ContractController controller;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        controller = ContractControllerFactory.getRemoteDatabaseController(connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void baseSearch(){

    }

}
