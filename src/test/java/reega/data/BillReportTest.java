package reega.data;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;
import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.remote.RemoteConnection;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static reega.data.utils.ContractUtils.insertContract;
import static reega.data.utils.FileUtils.getFileFromResourcesAsString;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillReportTest {
    private RemoteConnection connection;
    private ContractController contractController;
    private DataController dataController;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        contractController = ContractControllerFactory.getRemoteDatabaseController(connection);
        dataController = DataControllerFactory.getDefaultDataController(connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void ensureEmpty() throws IOException {
        var reports = contractController.getBillsForContracts(0);
        assertNotNull(reports);
        assertEquals(0, reports.size());
    }

    @Test
    @Order(2)
    public void insertData() throws IOException {
        var timestamp = 1615000000000L;
        insertContract(contractController, "Test Address", "ABC123", timestamp);
        var contracts = contractController.getUserContracts();
        assertNotNull(contracts);
        assertEquals(1, contracts.size());
        var contractID = contracts.get(0).getId();

        for (DataType type : DataType.values()) {
            final Data newData = new Data(contractID, type);
            newData.addRecord(timestamp + 1000, 5.5);
            newData.addRecord(timestamp + 2000, 6.4);
            newData.addRecord(timestamp + 3000, 7.3);
            dataController.putUserData(newData);
        }
    }

    @Test
    @Order(3)
    public void getBillReport() throws IOException, URISyntaxException {
        var contracts = contractController.getUserContracts();
        assertNotNull(contracts);
        assertEquals(1, contracts.size());

        var reports = contractController.getBillsForContracts(contracts.get(0).getId());
        assertNotNull(reports);
        assertEquals(1, reports.size());
        String report = reports.get(0).toString();
        String expected = getFileFromResourcesAsString("reports/report0.json");

        assertEquals(JsonParser.parseString(expected), JsonParser.parseString(report));
    }

}
