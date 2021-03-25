package reega.data.remote;

import java.io.IOException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.data.models.gson.LoginResponse;
import reega.data.models.gson.NewUserBody;
import reega.data.models.gson.UserAuthToken;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;
import retrofit2.Call;
import retrofit2.Response;

/**
 * AuthController implementation, using remote database via http requests
 */
public final class RemoteAuthAPI implements AuthController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteAuthAPI.class);
    private static RemoteAuthAPI INSTANCE;
    private final RemoteConnection connection;

    private RemoteAuthAPI(final RemoteConnection conn) {
        this.connection = Objects.requireNonNullElseGet(conn, RemoteConnection::new);
    }

    public synchronized static RemoteAuthAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteAuthAPI(null);
        }
        return INSTANCE;
    }

    public static synchronized RemoteAuthAPI getInstanceWithConnection(final RemoteConnection conn) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteAuthAPI(conn);
        }
        return INSTANCE;
    }

    @Override
    public void addUser(final NewUser newUser) throws IOException {
        final NewUserBody body = new NewUserBody(newUser.getName(), newUser.getSurname(), newUser.getEmail(),
                newUser.getFiscalCode(), newUser.getRole().getRoleName(), newUser.getPasswordHash());
        final Call<Void> v = this.connection.getService().addUser(body);
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void removeUser(String fiscalCode) throws IOException {
        Call<Void> v = connection.getService().removeUser(fiscalCode);
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public GenericUser emailLogin(final String email, final String password) throws IOException {
        return this.genericLogin(() -> {
            final Call<LoginResponse> r = this.connection.getService().emailLogin(email, password);
            return r.execute().body();
        });
    }

    @Override
    public GenericUser fiscalCodeLogin(final String fiscalCode, final String password) throws IOException {
        return this.genericLogin(() -> {
            final Call<LoginResponse> r = this.connection.getService().fiscalCodeLogin(fiscalCode, password);
            return r.execute().body();
        });
    }

    @Override
    public GenericUser tokenLogin(final UserAuth credentials) throws IOException {
        return this.genericLogin(() -> {
            final Call<LoginResponse> r = this.connection.getService()
                    .tokenCodeLogin(credentials.getSelector(), credentials.getValidator());
            return r.execute().body();
        });
    }

    private GenericUser genericLogin(final RemoteConnection.LoginMethod loginMethod) throws IOException {
        final LoginResponse response = this.connection.login(loginMethod);
        if (response == null) {
            return null;
        }
        return new GenericUser(Role.valueOf(response.role.toUpperCase()), response.name, response.surname,
                response.email, response.fiscalCode);
    }

    @Override
    public void storeUserCredentials(final String selector, final String validator) throws IOException {
        final Call<Void> v = this.connection.getService().storeUserToken(new UserAuthToken(selector, validator));
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void userLogout() throws IOException {
        final Call<Void> v = this.connection.getService().logout();
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

}
