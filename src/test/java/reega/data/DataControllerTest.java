package reega.data;

import org.junit.jupiter.api.*;
import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.remote.RemoteConnection;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static reega.data.utils.ContractUtils.insertContract;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataControllerTest {
    private RemoteConnection connection;
    private ContractController contractController;
    private DataController dataController;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        contractController = ContractControllerFactory.getRemoteDatabaseController(connection);
        dataController = DataControllerFactory.getDefaultDataController(connection);

        insertContract(contractController, "Test Address", "ABC123", 1614942000000L);
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void userDataTest() throws IOException {
        var contracts = contractController.getUserContracts();
        assertEquals(1, contracts.size());
        var contract = contracts.get(0);

        var latestTimestamp = dataController.getLatestData(contract.getId(), DataType.ELECTRICITY);
        // most be the contract start-time
        assertEquals(1614942000000L, latestTimestamp);

        final long timestamp = (System.currentTimeMillis() / 1000) * 1000;
        final Data newData = new Data(contract.getId(), DataType.ELECTRICITY);
        newData.addRecord(timestamp + 1000, 5.5);
        newData.addRecord(timestamp + 2000, 6.4);
        newData.addRecord(timestamp + 3000, 7.3);

        dataController.putUserData(newData);

        latestTimestamp = dataController.getLatestData(1, DataType.ELECTRICITY);
        assertEquals(timestamp + 3000, latestTimestamp);

        insertContract(contractController, "Address 2", "ABC123", 1615000000000L);
        // using same data, not relevant
        final Data data2 = new Data(contract.getId() + 1, DataType.ELECTRICITY, newData.getData());
        dataController.putUserData(data2);
    }

    @Test
    @Order(2)
    public void monthlyDataTest() throws IOException {
        var contracts = contractController.getUserContracts();
        assertEquals(2, contracts.size());

        // with contractID null should return data of all user contracts
        var data = dataController.getMonthlyData(null);
        var sum = data.stream().map(Data::getData).flatMap(k -> k.values().stream()).reduce(.0, Double::sum);
        assertEquals(2.0 * (5.5 + 6.4 + 7.3), sum);

        data = dataController.getMonthlyData(contracts.get(0).getId());
        sum = data.stream().map(Data::getData).flatMap(k -> k.values().stream()).reduce(.0, Double::sum);
        assertEquals(5.5 + 6.4 + 7.3, sum);
    }
}
