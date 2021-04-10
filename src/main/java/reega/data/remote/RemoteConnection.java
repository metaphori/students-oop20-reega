package reega.data.remote;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.models.gson.LoginResponse;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Handle the connection to the server and the http methods authentication
 */
public class RemoteConnection {
    private static final Logger logger = LoggerFactory.getLogger(RemoteConnection.class);
    private static Retrofit retrofit;
    private static ReegaService service;
    private static boolean forcedService = false;
    private String baseUrl;
    private String JWT;

    public RemoteConnection(final String baseUrl, boolean forceNewInstance) {
        this.baseUrl = baseUrl;
        buildRetrofit(forceNewInstance, null);
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
        logger.info("logging-in...");
        Response<LoginResponse> loginResponse = loginMethod.login();
        logger.info("response: " + loginResponse.code());
        if (loginResponse.code() > 299 || loginResponse.body() == null) {
            logger.info("error: " + loginResponse.errorBody());
            return null;
        }
        JWT = loginResponse.body().jwt;
        this.setClientAuth();

        return loginResponse.body();
    }

    public void logout() {
        this.JWT = null;
        buildRetrofit(true, null);
        service = retrofit.create(ReegaService.class);
    }

    private void buildRetrofit(boolean forceNewInstance, @Nullable OkHttpClient client) {
        if (forceNewInstance || retrofit == null) {
            var builder = new Retrofit.Builder();
            if (client != null) {
                builder.client(client);
            }
            retrofit = builder.baseUrl(baseUrl)
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

        buildRetrofit(true, client);
        service = retrofit.create(ReegaService.class);
    }

    public ReegaService getService() {
        return service;
    }

    public interface LoginMethod {
        Response<LoginResponse> login() throws IOException;
    }
}
