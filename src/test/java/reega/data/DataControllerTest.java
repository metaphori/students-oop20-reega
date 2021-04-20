package reega.data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public final class DataControllerTest {
    private RemoteConnection connection;
    private ContractController contractController;
    private DataController dataController;
    private static final long BASE_TIMESTAMP = 1614942000000L;

    @BeforeAll
    public void setup() throws IOException {
        this.connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        this.contractController = ContractControllerFactory.getRemoteDatabaseController(this.connection);
        this.dataController = DataControllerFactory.getDefaultDataController(this.connection);

        ContractUtils.insertContract(this.contractController, "Test Address", "ABC123",
                DataControllerTest.BASE_TIMESTAMP);
    }

    @AfterAll
    public void cleanup() throws IOException {
        this.connection.getService().terminateTest().execute();
        this.connection.logout();
    }

    @Test
    @Order(1)
    public void ensureEmpty() throws IOException {
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertEquals(1, contracts.size());
        final var report = this.contractController.getBillsForContracts(List.of(contracts.get(0).getId()));
        Assertions.assertNotNull(report);
        Assertions.assertEquals(0, report.size());
        report.forEach(System.out::println);
    }

    @Test
    @Order(2)
    public void userDataTest() throws IOException {
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertEquals(1, contracts.size());
        final var contract = contracts.get(0);

        var latestTimestamp = this.dataController.getLatestData(contract.getId(), DataType.ELECTRICITY);
        // most be the contract start-time
        Assertions.assertEquals(DataControllerTest.BASE_TIMESTAMP, latestTimestamp);

        final long timestamp = DataControllerTest.BASE_TIMESTAMP + 5000;
        final Data newData = new Data(contract.getId(), DataType.ELECTRICITY);
        newData.addRecord(timestamp + 1000, 5.5);
        newData.addRecord(timestamp + 2000, 6.4);
        newData.addRecord(timestamp + 3000, 7.3);

        this.dataController.putUserData(newData);
        latestTimestamp = this.dataController.getLatestData(1, DataType.ELECTRICITY);
        Assertions.assertEquals(timestamp + 3000, latestTimestamp);

        ContractUtils.insertContract(this.contractController, "Address 2", "ABC123", DataControllerTest.BASE_TIMESTAMP);
        final var currentTimesamp = (System.currentTimeMillis() / 10000) * 10000;
        final var secondContractData = newData.getData()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(k -> currentTimesamp + (k.getKey() % 10000), Map.Entry::getValue));
        secondContractData.forEach((k, v) -> System.out.println("key: " + k + " value: " + v));
        // using same data, not relevant
        final Data data2 = new Data(contract.getId() + 1, DataType.ELECTRICITY, secondContractData);
        this.dataController.putUserData(data2);
    }

    @Test
    @Order(3)
    public void monthlyDataTest() throws IOException {
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertEquals(2, contracts.size());

        // with contractID null should return data of all user contracts
        var data = this.dataController.getMonthlyData(null);
        var sum = data.stream().map(Data::getData).flatMap(k -> k.values().stream()).reduce(.0, Double::sum);
        Assertions.assertEquals(5.5 + 6.4 + 7.3, sum);

        data = this.dataController.getMonthlyData(contracts.get(0).getId());
        sum = data.stream().map(Data::getData).flatMap(k -> k.values().stream()).reduce(.0, Double::sum);
        Assertions.assertEquals(.0, sum);

        data = this.dataController.getMonthlyData(contracts.get(1).getId());
        sum = data.stream().map(Data::getData).flatMap(k -> k.values().stream()).reduce(.0, Double::sum);
        Assertions.assertEquals(5.5 + 6.4 + 7.3, sum);
    }

    @Test
    @Order(4)
    public void billReport() throws IOException, URISyntaxException {
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertEquals(2, contracts.size());
        final var report = this.contractController.getBillsForContracts(List.of(contracts.get(0).getId()));
        Assertions.assertEquals(1, report.size());

        final var expected = FileUtils.getFileFromResourcesAsString("reports/report1.json");
        Assertions.assertEquals(JsonParser.parseString(expected), JsonParser.parseString(report.get(0).toString()));
    }
}
