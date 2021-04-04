package reega.data.mock;

import reega.data.remote.ReegaService;
import reega.data.remote.RemoteConnection;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class TestConnection {
    private String baseUrl = "http://52.208.47.221:1958/test/";

    public RemoteConnection getTestConnection(String email, String psk) throws IOException {
        final ReegaService tmpService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ReegaService.class);

        final String testDB = tmpService.initTest().execute().body();
        baseUrl += testDB + "/";

        RemoteConnection connection = new RemoteConnection(baseUrl);
        System.out.println("connection created with base url " + baseUrl);
        connection.login(connection.getService().emailLogin(email, psk).execute()::body);
        return connection;
    }
}
