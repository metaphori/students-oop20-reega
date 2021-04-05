package reega.data.remote;

import okhttp3.OkHttpClient;
import reega.data.models.gson.LoginResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Handle the connection to the server and the http methods authentication
 */
public class RemoteConnection {
    private static Retrofit retrofit;
    private static ReegaService service;
    private static boolean forcedService = false;
    private String baseUrl;
    private String JWT;

    public RemoteConnection(final String baseUrl, boolean forceNewInstance) {
        this.baseUrl = baseUrl;
        createPlainClient(forceNewInstance);
        service = retrofit.create(ReegaService.class);
    }

    public RemoteConnection(String baseUrl) {
        this(baseUrl, false);
    }

    public RemoteConnection() {
        this("http://52.208.47.221:1958/");
    }

    /**
     * Initialize the connection with the given service
     *
     * @param s
     */
    @Deprecated
    public RemoteConnection(final ReegaService s) {
        forcedService = true;
        service = s;
    }

    @Deprecated
    public void overrideToken(final String JWT) {
        this.JWT = JWT;
        this.setClientAuth();
    }

    public LoginResponse login(final LoginMethod loginMethod) throws IOException {
        final LoginResponse response = loginMethod.login();
        if (response != null) {
            JWT = response.jwt;
            this.setClientAuth();
        }
        return response;
    }

    public void logout() {
        this.JWT = null;
        createPlainClient(true);
        service = retrofit.create(ReegaService.class);
    }

    private void createPlainClient(boolean forceNewInstance) {
        if (forceNewInstance || retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
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

    public ReegaService getService() {
        return service;
    }

    public interface LoginMethod {
        LoginResponse login() throws IOException;
    }
}
