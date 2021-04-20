package reega.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static reega.data.utils.ContractUtils.insertContract;
import static reega.data.utils.FileUtils.getFileFromResourcesAsString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.*;

import com.google.gson.JsonParser;

import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.remote.RemoteConnection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillReportTest {
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
        assertNotNull(reports);
        assertEquals(0, reports.size());
    }

    @Test
    @Order(2)
    public void insertData() throws IOException {
        final var timestamp = 1615000000000L;
        insertContract(this.contractController, "Test Address", "ABC123", timestamp);
        final var contracts = this.contractController.getUserContracts();
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
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
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        final var reports = this.contractController.getBillsForContracts(List.of(contracts.get(0).getId()));
        assertNotNull(reports);
        assertEquals(1, reports.size());
        final String report = reports.get(0).toString();
        final String expected = getFileFromResourcesAsString("reports/report0.json");

        assertEquals(JsonParser.parseString(expected), JsonParser.parseString(report));
    }
}
