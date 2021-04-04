package reega.data.mock;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;
import reega.data.remote.ReegaService;
import reega.data.remote.RemoteAuthAPI;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteDatabaseAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MockConnection implements AutoCloseable {
    private static MockWebServer server;
    private static RemoteDatabaseAPI databaseAPI;
    private static RemoteAuthAPI authAPI;
    private static Retrofit retrofit;

    public MockConnection(final Dispatcher dispatcher) throws IOException {
        server = new MockWebServer();
        server.setDispatcher(dispatcher);
        server.start();

        final ReegaService service = new Retrofit.Builder().baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ReegaService.class);
        final RemoteConnection connection = new RemoteConnection(service);
        databaseAPI = RemoteDatabaseAPI.getInstance(connection);
        authAPI = RemoteAuthAPI.getInstance(connection);
    }

    public MockConnection(final String remoteURL) {
        RemoteConnection connection = new RemoteConnection(remoteURL);
        databaseAPI = RemoteDatabaseAPI.getInstance(connection);
        authAPI = RemoteAuthAPI.getInstance(connection);
    }

    public RemoteDatabaseAPI getDatabaseAPI() {
        return databaseAPI;
    }

    public RemoteAuthAPI getAuthAPI() {
        return authAPI;
    }

    @Override
    public void close() throws IOException {
        if (server != null) {
            server.shutdown();
        }
    }
}
