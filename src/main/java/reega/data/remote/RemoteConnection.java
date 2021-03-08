package reega.data.remote;

import okhttp3.OkHttpClient;
import reega.data.remote.models.LoginResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Handle the connection to the server and the http methods authentication
 */
public class RemoteConnection {
    private static final String baseUrl = "http://52.30.34.181/";
    private static String JWT;
    private static Retrofit retrofit;
    private static ReegaService service;
    private static boolean forcedService = false;

    interface LoginMethod {
        LoginResponse login() throws IOException;
    }

    public RemoteConnection() {
        // build base retrofit
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        service = retrofit.create(ReegaService.class);
    }

    /**
     * Initialize the connection with the given service
     *
     * @param s
     */
    public RemoteConnection(final ReegaService s) {
        forcedService = true;
        service = s;
    }

    public LoginResponse login(final LoginMethod loginMethod) throws IOException {
        final LoginResponse response = loginMethod.login();
        if (response != null) {
            JWT = response.jwt;
            this.setClientAuth();
        }
        return response;
    }

    private void setClientAuth() {
        // not touching the service as it's been specified at init time
        if (forcedService) {
            return;
        }
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain
                        .proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + JWT).build()))
                .build();
        // building retrofit service with authenticator
        retrofit = new Retrofit.Builder().client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ReegaService.class);
    }

    ReegaService getService() {
        return service;
    }
}
