package reega.data.remote;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.data.remote.models.LoginResponse;
import reega.data.remote.models.NewUserBody;
import reega.data.remote.models.UserAuthToken;
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

    private RemoteAuthAPI() {
        connection = new RemoteConnection();
    }

    public synchronized static RemoteAuthAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteAuthAPI();
        }
        return INSTANCE;
    }

    @Override
    public void addUser(NewUser newUser) throws IOException {
        NewUserBody body = new NewUserBody(
                newUser.getName(),
                newUser.getSurname(),
                newUser.getEmail(),
                newUser.getFiscalCode(),
                newUser.getRole().getRoleName(),
                newUser.getPasswordHash());
        Call<Void> v = connection.getService().addUser(body);
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public GenericUser emailLogin(String email, String password) throws IOException {
        return genericLogin(() -> {
            Call<LoginResponse> r = connection.getService().emailLogin(email, password);
            return r.execute().body();
        });
    }

    @Override
    public GenericUser fiscalCodeLogin(String fiscalCode, String password) throws IOException {
        return genericLogin(() -> {
            Call<LoginResponse> r = connection.getService().fiscalCodeLogin(fiscalCode, password);
            return r.execute().body();
        });
    }

    @Override
    public GenericUser tokenLogin(UserAuth credentials) throws IOException {
        return genericLogin(() -> {
            Call<LoginResponse> r = connection.getService().tokenCodeLogin(
                    credentials.getSelector(), credentials.getValidator()
            );
            return r.execute().body();
        });
    }

    private GenericUser genericLogin(RemoteConnection.LoginMethod loginMethod) throws IOException {
        final LoginResponse response = connection.login(loginMethod);
        if (response == null) {
            return null;
        }
        return new GenericUser(
                Role.valueOf(response.role.toUpperCase()),
                response.name,
                response.surname,
                response.email,
                response.fiscalCode);
    }

    @Override
    public void storeUserCredentials(String selector, String validator) throws IOException {
        Call<Void> v = connection.getService().storeUserToken(new UserAuthToken(selector, validator));
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void userLogout() throws IOException {
        Call<Void> v = connection.getService().logout();
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

}
