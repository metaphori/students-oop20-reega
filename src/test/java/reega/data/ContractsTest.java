package reega.data;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import reega.data.factory.ContractControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class ContractsTest {
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
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(0, contracts.size());

        contracts = this.controller.searchContract("reega");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(0, contracts.size());
    }

    @Test
    @Order(2)
    public void addContract() throws IOException {
        final List<ServiceType> services = List.of(ServiceType.GAS, ServiceType.WATER, ServiceType.GARBAGE,
                ServiceType.ELECTRICITY);
        final NewContract newContract = new NewContract("Test Address", services, "ABC123", new Date(1614942000000L));
        final var insertedContract = this.controller.addContract(newContract);
        Assertions.assertNotNull(insertedContract);
    }

    @Test
    @Order(3)
    public void searchContracts() throws IOException {
        var contracts = this.controller.searchContract("ABC123");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("not existing");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(0, contracts.size());

        contracts = this.controller.searchContract("test");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("test address");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("ABC123");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("reega");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());

        contracts = this.controller.searchContract("admin");
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());
    }

    @Test
    @Order(4)
    public void getUserContracts() throws IOException {
        final var contracts = this.controller.getUserContracts();
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());
        final Contract contract = contracts.get(0);
        Assertions.assertEquals(new Date(1614942000000L), contract.getStartDate());
    }

    @Test
    @Order(5)
    public void getAllContracts() throws IOException {
        final var contracts = this.controller.getAllContracts();
        Assertions.assertEquals(1, contracts.size());
    }

    @Test
    @Order(6)
    public void getSpecificUserContracts() throws IOException {
        final var contracts = this.controller.getContractsForUser("ABC123");
        Assertions.assertEquals(1, contracts.size());
        Assertions.assertEquals(new Date(1614942000000L), contracts.get(0).getStartDate());
    }

    @Test
    @Order(7)
    public void deleteContract() throws IOException {
        var contracts = this.controller.getUserContracts();
        Assertions.assertEquals(1, contracts.size());
        final var contractID = contracts.get(0).getId();
        this.controller.removeContract(contractID);

        contracts = this.controller.getUserContracts();
        Assertions.assertEquals(0, contracts.size());
    }
}
