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
 * Handle the connection to the server and the http methods authentication.
 */
public class RemoteConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteConnection.class);
    private static final int INITIAL_ERROR_CODE = 299;
    private static Retrofit retrofit;
    private static ReegaService service;
    private final String baseUrl;
    private String jwt;

    /**
     * Get connection instance.
     *
     * @param baseUrl          base URL as prefix fot http paths
     * @param forceNewInstance if set to true, force the creation of a new instance
     */
    public RemoteConnection(final String baseUrl, final boolean forceNewInstance) {
        this.baseUrl = baseUrl;
        this.buildRetrofit(forceNewInstance, null);
        RemoteConnection.service = RemoteConnection.retrofit.create(ReegaService.class);
    }

    /**
     * Get connection instance.
     *
     * @param baseUrl base URL as prefix fot http paths
     */
    public RemoteConnection(final String baseUrl) {
        this(baseUrl, false);
    }

    /**
     * Get connection instance using the default URL.
     */
    public RemoteConnection() {
        this("http://52.208.47.221:1958/");
    }

    /**
     * Override the current JWT Token.
     *
     * @param jwt JWT Token
     */
    public void overrideToken(final String jwt) {
        this.jwt = jwt;
        this.setClientAuth();
    }

    /**
     * Login into the REEGA Platform.
     *
     * @param loginMethod method of login
     * @return a {@link LoginResponse} containing returned data
     * @throws IOException if an error occurred while performing the HTTP call
     */
    public LoginResponse login(final LoginMethod loginMethod) throws IOException {
        RemoteConnection.LOGGER.info("logging-in...");
        final Response<LoginResponse> loginResponse = loginMethod.login();
        RemoteConnection.LOGGER.info("response: " + loginResponse.code());
        if (loginResponse.code() > RemoteConnection.INITIAL_ERROR_CODE || loginResponse.body() == null) {
            RemoteConnection.LOGGER.info("error: " + loginResponse.errorBody());
            return null;
        }
        this.jwt = loginResponse.body().jwt;
        this.setClientAuth();

        return loginResponse.body();
    }

    /**
     * Logout from the REEGA Platform.
     * This will drop the jwt authenticator middleware and recreate the HTTP client.
     */
    public void logout() {
        this.jwt = null;
        this.buildRetrofit(true, null);
        RemoteConnection.service = RemoteConnection.retrofit.create(ReegaService.class);
    }

    private void buildRetrofit(final boolean forceNewInstance, @Nullable final OkHttpClient client) {
        if (forceNewInstance || RemoteConnection.retrofit == null) {
            final var builder = new Retrofit.Builder();
            if (client != null) {
                builder.client(client);
            }
            RemoteConnection.retrofit = builder.baseUrl(this.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    private void setClientAuth() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain
                        .proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + this.jwt).build()))
                .build();

        this.buildRetrofit(true, client);
        RemoteConnection.service = RemoteConnection.retrofit.create(ReegaService.class);
    }

    /**
     * Get the associated {@link ReegaService}.
     *
     * @return the associated {@link ReegaService}
     */
    public ReegaService getService() {
        return RemoteConnection.service;
    }

    /**
     * Basic interface to allow multiple login methods with similar logic.
     */
    public interface LoginMethod {
        Response<LoginResponse> login() throws IOException;
    }
}
