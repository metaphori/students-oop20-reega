package reega.data.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.data.UserController;
import reega.data.models.gson.NewUserBody;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.User;
import retrofit2.Response;

/**
 * {@link UserController} implementation, using remote database via http requests.
 */
public class RemoteUserAPI implements UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteUserAPI.class);
    private static final int INITIAL_ERROR_CODE = 299;
    private final RemoteConnection connection;

    public RemoteUserAPI(final RemoteConnection c) {
        this.connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUser(final NewUser newUser) throws IOException {
        RemoteUserAPI.LOGGER.info("adding user: " + newUser.toString());
        final NewUserBody body = new NewUserBody(newUser.getName(), newUser.getSurname(), newUser.getEmail(),
                newUser.getFiscalCode(), newUser.getRole().getRoleName(), newUser.getPasswordHash());
        final Response<Void> r = this.connection.getService().addUser(body).execute();
        RemoteUserAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteUserAPI.INITIAL_ERROR_CODE) {
            RemoteUserAPI.LOGGER.info("error: " + r.errorBody());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(final String fiscalCode) throws IOException {
        RemoteUserAPI.LOGGER.info("removing user with fiscal code: " + fiscalCode);
        final Response<Void> r = this.connection.getService().removeUser(fiscalCode).execute();
        RemoteUserAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteUserAPI.INITIAL_ERROR_CODE) {
            RemoteUserAPI.LOGGER.info("error: " + r.errorBody());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserFromContract(final int contractID) throws IOException {
        RemoteUserAPI.LOGGER.info("getting user with contract ID: " + contractID);
        final Response<reega.data.models.gson.User> r = this.connection.getService()
                .getUserFromContract(contractID)
                .execute();
        RemoteUserAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteUserAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteUserAPI.LOGGER.info("error: " + r.errorBody());
            return null;
        }
        return new GenericUser(r.body());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> searchUser(final String keyword) throws IOException {
        RemoteUserAPI.LOGGER.info("searching users with keyword: " + keyword);
        final Response<List<reega.data.models.gson.User>> r = this.connection.getService()
                .searchUser(keyword)
                .execute();
        RemoteUserAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteUserAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteUserAPI.LOGGER.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body().stream().map(GenericUser::new).collect(Collectors.toList());
    }
}
