package reega.data.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.data.models.gson.LoginResponse;
import reega.data.models.gson.UserAuthToken;
import reega.users.GenericUser;
import reega.users.Role;
import reega.users.User;
import retrofit2.Response;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;

/**
 * AuthController implementation, using remote database via http requests
 */
public final class RemoteAuthAPI implements AuthController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteAuthAPI.class);
    private final RemoteConnection connection;

    public RemoteAuthAPI(@Nullable final RemoteConnection connection) {
        this.connection = Objects.requireNonNullElseGet(connection, RemoteConnection::new);
    }

    @Override
    public User emailLogin(final String email, final String password) throws IOException {
        return this.genericLogin(() -> this.connection.getService().emailLogin(email, password).execute());
    }

    @Override
    public User fiscalCodeLogin(final String fiscalCode, final String password) throws IOException {
        return this.genericLogin(() -> this.connection.getService().fiscalCodeLogin(fiscalCode, password).execute());
    }

    @Override
    public User tokenLogin(final UserAuth credentials) throws IOException {
        return this.genericLogin(() -> this.connection.getService()
                .tokenCodeLogin(credentials.getSelector(), credentials.getValidator()).execute());
    }

    private User genericLogin(final RemoteConnection.LoginMethod loginMethod) throws IOException {
        final LoginResponse response = this.connection.login(loginMethod);
        return response == null ? null :
                new GenericUser(Role.valueOf(response.role.toUpperCase()), response.name, response.surname,
                        response.email, response.fiscalCode);
    }

    @Override
    public void storeUserCredentials(final String selector, final String validator) throws IOException {
        final Response<Void> r = this.connection.getService().storeUserToken(new UserAuthToken(selector, validator)).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299) {
            logger.info("error: " + r.errorBody());
        }
    }

    @Override
    public void userLogout() throws IOException {
        final Response<Void> r = this.connection.getService().logout().execute();
        logger.info("response: " + r.code());
        if (r.code() > 299) {
            logger.info("error: " + r.errorBody());
        }
    }

}
