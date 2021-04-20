package reega.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.*;

import reega.data.factory.ContractControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractsTest {
    private RemoteConnection connection;
    private ContractController controller;

    @BeforeAll
    public void setup() throws IOException {
        this.connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        this.controller = ContractControllerFactory.getRemoteDatabaseController(this.connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        this.connection.getService().terminateTest().execute();
        this.connection.logout();
    }

    @Test
    @Order(1)
    public void ensureEmpty() throws IOException {
        var contracts = this.controller.getUserContracts();
        assertNotNull(contracts);
        assertEquals(0, contracts.size());

        contracts = this.controller.searchContract("reega");
        assertNotNull(contracts);
        assertEquals(0, contracts.size());
    }

    @Test
    @Order(2)
    public void addContract() throws IOException {
        final List<ServiceType> services = List.of(ServiceType.GAS, ServiceType.WATER, ServiceType.GARBAGE,
                ServiceType.ELECTRICITY);
        final NewContract newContract = new NewContract("Test Address", services, "ABC123", new Date(1614942000000L));
        final var insertedContract = this.controller.addContract(newContract);
        assertNotNull(insertedContract);
    }

    @Test
    @Order(3)
    public void searchContracts() throws IOException {
        var contracts = this.controller.searchContract("ABC123");
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("not existing");
        assertNotNull(contracts);
        assertEquals(0, contracts.size());

        contracts = this.controller.searchContract("test");
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("test address");
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("ABC123");
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("reega");
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("admin");
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
    }

    @Test
    @Order(4)
    public void getUserContracts() throws IOException {
        final var contracts = this.controller.getUserContracts();
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
        final Contract contract = contracts.get(0);
        assertEquals(new Date(1614942000000L), contract.getStartDate());
    }

    @Test
    @Order(5)
    public void getAllContracts() throws IOException {
        final var contracts = this.controller.getAllContracts();
        assertEquals(1, contracts.size());
    }

    @Test
    @Order(6)
    public void getSpecificUserContracts() throws IOException {
        final var contracts = this.controller.getContractsForUser("ABC123");
        assertEquals(1, contracts.size());
        assertEquals(new Date(1614942000000L), contracts.get(0).getStartDate());
    }

    @Test
    @Order(7)
    public void deleteContract() throws IOException {
        var contracts = this.controller.getUserContracts();
        assertEquals(1, contracts.size());
        final var contractID = contracts.get(0).getId();
        this.controller.removeContract(contractID);

        contracts = this.controller.getUserContracts();
        assertEquals(0, contracts.size());
    }
}