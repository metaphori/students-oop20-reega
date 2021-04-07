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
public class ContractsTest {
    private RemoteConnection connection;
    private DataController controller;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        controller = DataControllerFactory.getRemoteDatabaseController(connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void ensureEmpty() throws IOException {
        var contracts = controller.getUserContracts();
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
        controller.addContract(newContract);
    }

    @Test
    @Order(3)
    public void getUserContracts() throws IOException {
        var contracts = controller.getUserContracts();
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
        Contract contract = contracts.get(0);
        assertEquals(new Date(1614942000000L), contract.getStartDate());
    }

    @Test
    @Order(4)
    public void getAllContracts() throws IOException {
        var contracts = controller.getAllContracts();
        assertEquals(1, contracts.size());
    }

    @Test
    @Order(5)
    public void getSpecificUserContracts() throws IOException {
        var contracts = controller.getContractsForUser("ABC123");
        assertEquals(1, contracts.size());
        assertEquals(new Date(1614942000000L), contracts.get(0).getStartDate());
    }

    @Test
    @Order(7)
    public void deleteContract() throws IOException {
        var contracts = controller.getUserContracts();
        assertEquals(1, contracts.size());
        var contractID = contracts.get(0).getId();
        controller.removeContract(contractID);

        contracts = controller.getUserContracts();
        assertEquals(0, contracts.size());
    }
}