package reega.data.mock;

import java.io.IOException;

import reega.data.remote.ReegaService;
import reega.data.remote.RemoteConnection;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestConnection {
    private String baseUrl = "http://52.208.47.221:1958/test/";

    public RemoteConnection getTestConnection(final String email, final String psk) throws IOException {
        final ReegaService tmpService = new Retrofit.Builder().baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReegaService.class);

        final String testDB = tmpService.initTest().execute().body();
        this.baseUrl += testDB + "/";

        final RemoteConnection connection = new RemoteConnection(this.baseUrl, true);
        System.out.println("connection created with base url " + this.baseUrl);
        connection.login(() -> connection.getService().emailLogin(email, psk).execute());
        return connection;
    }
}
