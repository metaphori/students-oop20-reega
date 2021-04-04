package reega.data;

import org.junit.jupiter.api.*;
import reega.data.mock.TestConnection;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FullDataTest {
    private static RemoteConnection connection;
    private static DataController dataController;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        dataController = DataControllerFactory.getRemoteDatabaseController(connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
    }

    @Test
    @Order(1)
    public void ensureEmpty() throws IOException {
        var contracts = dataController.getUserContracts();
        assertNotNull(contracts);
        assertEquals(0, contracts.size());
    }

    @Test
    @Order(2)
    public void addContract() throws IOException {
        List<ServiceType> services = List.of(
                ServiceType.GAS,
                ServiceType.WATER,
                ServiceType.GARBAGE,
                ServiceType.ELECTRICITY
        );
        NewContract newContract = new NewContract("Test Address", services, "ABC123", new Date(1614942000000L));
        dataController.addContract(newContract);
    }

    @Test
    @Order(3)
    public void getUserContracts() throws IOException {
        var contracts = dataController.getUserContracts();
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
        Contract contract = contracts.get(0);
        assertEquals(new Date(1614942000000L), contract.getStartDate());
    }
}
