package reega.data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.gson.JsonParser;

import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.remote.RemoteConnection;
import reega.data.utils.ContractUtils;
import reega.data.utils.FileUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class BillReportTest {
    private RemoteConnection connection;
    private ContractController contractController;
    private DataController dataController;

    @BeforeAll
    public void setup() throws IOException {
        this.connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        this.contractController = ContractControllerFactory.getRemoteDatabaseController(this.connection);
        this.dataController = DataControllerFactory.getDefaultDataController(this.connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        this.connection.getService().terminateTest().execute();
        this.connection.logout();
    }

    @Test
    @Order(1)
    public void ensureEmpty() throws IOException {
        final var reports = this.contractController.getBillsForContracts(List.of(0));
        Assertions.assertNotNull(reports);
        Assertions.assertEquals(0, reports.size());
    }

    @Test
    @Order(2)
    public void insertData() throws IOException {
        final var timestamp = 1615000000000L;
        ContractUtils.insertContract(this.contractController, "Test Address", "ABC123", timestamp);
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());
        final var contractID = contracts.get(0).getId();

        for (final DataType type : DataType.values()) {
            final Data newData = new Data(contractID, type);
            newData.addRecord(timestamp + 1000, 5.5);
            newData.addRecord(timestamp + 2000, 6.4);
            newData.addRecord(timestamp + 3000, 7.3);
            this.dataController.putUserData(newData);
        }
    }

    @Test
    @Order(3)
    public void getBillReport() throws IOException, URISyntaxException {
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertNotNull(contracts);
        Assertions.assertEquals(1, contracts.size());

        final var reports = this.contractController.getBillsForContracts(List.of(contracts.get(0).getId()));
        Assertions.assertNotNull(reports);
        Assertions.assertEquals(1, reports.size());
        final String report = reports.get(0).toString();
        final String expected = FileUtils.getFileFromResourcesAsString("reports/report0.json");

        Assertions.assertEquals(JsonParser.parseString(expected), JsonParser.parseString(report));
    }
}
