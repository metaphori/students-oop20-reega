package reega.data;

import okhttp3.mockwebserver.Dispatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reega.data.mock.MockConnection;
import reega.data.mock.MockedDataService;
import reega.data.mock.RequestDispatcher;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.remote.RemoteDatabaseAPI;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataControllerTest {
    private static MockConnection connection;
    private static RemoteDatabaseAPI databaseAPI;

    @BeforeAll
    static void setup() throws IOException {
        // TODO replace this with method to add and menage contracts
        final Contract c = new Contract(1, "address", List.of("electricity"), new Date());
        final Dispatcher dispatcher = new RequestDispatcher(new MockedDataService(c), null);
        connection = new MockConnection(dispatcher);
        databaseAPI = connection.getDatabaseAPI();
    }

    @AfterAll
    static void cleanup() throws IOException {
        connection.close();
    }

    @Test
    public void getContractsTest() throws IOException {
        final List<Contract> contracts = databaseAPI.getUserContracts();

        // test the contract has been parsed successfully
        assertEquals(contracts.size(), 1);

        // test the date parsing (timezone, etc)
        final Date startTime = new Date(1610959389000L);
        assertEquals(startTime, contracts.get(0).getStartDate());

        // general test
        assertEquals(contracts.get(0).toString(),
                "Contract{id=1, address='Casa sua', services=[GARBAGE, ELECTRICITY, GAS, WATER], "
                        + "priceModel=PriceModel{id=1, name='base plan', prices={electricity=1.4, garbage=1.3, "
                        + "gas=1.1, water=1.2}}, startDate=Mon Jan 18 09:43:09 CET 2021}");
    }

    @Test
    public void userDataTest() throws IOException {
        final long timestamp = (System.currentTimeMillis() / 1000) * 1000;
        final Data newData = new Data(1, DataType.ELECTRICITY);
        newData.addRecord(timestamp + 1000, 5.5);
        newData.addRecord(timestamp + 2000, 6.4);
        newData.addRecord(timestamp + 3000, 7.3);

        databaseAPI.putUserData(newData);

        final long latestTimestamp = databaseAPI.getLatestData(1, DataType.ELECTRICITY);
        // +3.600.000 because we insert data in UTC, and we get them as GMT
        assertEquals(timestamp + 3600000 + 3000, latestTimestamp);
    }
}
