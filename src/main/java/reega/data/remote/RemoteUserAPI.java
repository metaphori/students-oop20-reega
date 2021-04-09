package reega.data.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.UserController;
import reega.data.models.gson.NewUserBody;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.User;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RemoteUserAPI implements UserController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteUserAPI.class);
    private final RemoteConnection connection;

    public RemoteUserAPI(final RemoteConnection c) {
        connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    @Override
    public void addUser(final NewUser newUser) throws IOException {
        logger.info("adding user: " + newUser.toString());
        final NewUserBody body = new NewUserBody(newUser.getName(), newUser.getSurname(), newUser.getEmail(),
                newUser.getFiscalCode(), newUser.getRole().getRoleName(), newUser.getPasswordHash());
        final Response<Void> r = this.connection.getService().addUser(body).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299) {
            logger.info("error: " + r.errorBody());
        }
    }

    @Override
    public void removeUser(final String fiscalCode) throws IOException {
        logger.info("removing user with fiscal code: " + fiscalCode);
        final Response<Void> r = connection.getService().removeUser(fiscalCode).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299) {
            logger.info("error: " + r.errorBody());
        }
    }

    @Override
    public User getUserFromContract(final int contractID) throws IOException {
        logger.info("getting user with contract ID: " + contractID);
        final Response<reega.data.models.gson.User> r = connection.getService().getUserFromContract(contractID).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return null;
        }
        return new GenericUser(r.body());
    }

    @Override
    public List<User> searchUser(final String keyword) throws IOException {
        logger.info("searching users with keyword: " + keyword);
        final Response<List<reega.data.models.gson.User>> r = connection.getService().searchUser(keyword).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body().stream().map(GenericUser::new).collect(Collectors.toList());
    }
}
