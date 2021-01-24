package reega.data;

import okhttp3.mockwebserver.MockWebServer;
import reega.data.models.Contract;
import reega.data.models.PriceModel;
import reega.data.remote.ReegaService;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteDatabaseAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class MockedDataController implements AutoCloseable {
    private static MockWebServer server;
    private static RemoteDatabaseAPI databaseAPI;

    public MockedDataController() {
        PriceModel pm = new PriceModel(1, "test_pm", Map.of("electricity", 1.5));
        Contract c = new Contract(1, "address", List.of("electricity"), pm, new Date());
        MockedDataService dataService = new MockedDataService(c);

        server = new MockWebServer();
        server.setDispatcher(new RequestDispatcher(dataService));
        try {
            server.start();
        } catch (IOException e) {
            fail();
        }
        final ReegaService service = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReegaService.class);
        RemoteConnection connection = new RemoteConnection(service);
        databaseAPI = RemoteDatabaseAPI.getInstanceWithConnection(connection);
        assertNotNull(databaseAPI);
    }

    public RemoteDatabaseAPI getDatabaseAPI() {
        return databaseAPI;
    }

    @Override
    public void close() throws IOException {
        server.shutdown();
    }
}
