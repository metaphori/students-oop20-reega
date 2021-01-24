package reega.data;

import okhttp3.mockwebserver.Dispatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.PriceModel;
import reega.data.models.ServiceType;
import reega.data.remote.RemoteDatabaseAPI;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataControllerTest {
    private static MockConnection connection;
    private static RemoteDatabaseAPI databaseAPI;

    @BeforeAll
    static void setup() throws IOException {
        // TODO replace this with method to add and menage contracts
        PriceModel pm = new PriceModel(1, "test_pm", Map.of("electricity", 1.5));
        Contract c = new Contract(1, "address", List.of("electricity"), pm, new Date());
        Dispatcher dispatcher = new RequestDispatcher(new MockedDataService(c));
        connection = new MockConnection(dispatcher);
        databaseAPI = connection.getDatabaseAPI();
    }

    @AfterAll
    static void cleanup() throws IOException {
        connection.close();
    }

    @Test
    public void getContractsTest() throws IOException {
        List<Contract> contracts = databaseAPI.getUserContracts();

        // test the contract has been parsed successfully
        assertEquals(contracts.size(), 1);

        // test the date parsing (timezone, etc)
        Date startTime = new Date(1610959389000L);
        assertEquals(startTime, contracts.get(0).getStartDate());

        // general test
        assertEquals(contracts.get(0).toString(),
                "Contract{id=1, address='Casa sua', services=[GARBAGE, ELECTRICITY, GAS, WATER], " +
                        "priceModel=PriceModel{id=1, name='base plan', prices={electricity=1.4, garbage=1.3, " +
                        "gas=1.1, water=1.2}}, startDate=Mon Jan 18 09:43:09 CET 2021}");
    }

    @Test
    public void userDataTest() throws IOException {
        long timestamp = (System.currentTimeMillis() / 1000) * 1000;
        Data newData = new Data(1, ServiceType.ELECTRICITY);
        newData.addRecord(timestamp + 1000, 5.5);
        newData.addRecord(timestamp + 2000, 6.4);
        newData.addRecord(timestamp + 3000, 7.3);

        databaseAPI.putUserData(newData);

        long latestTimestamp = databaseAPI.getLatestData(1, ServiceType.ELECTRICITY);
        // +3.600.000 because we insert data in UTC, and we get them as GMT
        assertEquals(timestamp + 3600000 + 3000, latestTimestamp);
    }
}
