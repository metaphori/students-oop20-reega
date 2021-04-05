package reega.data.remote;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.gson.ContractModel;
import reega.data.models.gson.DataModel;
import reega.data.models.gson.NewContract;
import reega.users.GenericUser;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DataController implementation, using remote database via http requests
 */
public class RemoteDatabaseAPI implements DataController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteDatabaseAPI.class);
    private static RemoteConnection connection;
    private static RemoteDatabaseAPI INSTANCE;

    private RemoteDatabaseAPI(final RemoteConnection c) {
        connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    public synchronized static RemoteDatabaseAPI getInstance(final RemoteConnection connection) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDatabaseAPI(connection);
        }

        return INSTANCE;
    }

    @Override
    public void putUserData(final Data data) throws IOException {
        final Call<Void> v = connection.getService().pushData(data.getJsonModel());
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public Long getLatestData(final int contractID, final DataType service) throws IOException {
        final Call<Date> v = connection.getService().getLatestData(service.getID(), contractID);
        final Response<Date> r = v.execute();
        final Date d = r.body();
        return d == null ? 0L : d.getTime();
    }

    @Override
    public List<Data> getMonthlyData(@Nullable Integer contractID) throws IOException {
        final Map<String, String> options = new HashMap<>();
        if (contractID != null) {
            options.put("contract_id", String.valueOf(contractID));
        }
        final Call<List<DataModel>> v = connection.getService().getMonthlyData(options);
        final Response<List<DataModel>> r = v.execute();
        if (r.body() == null) {
            //TODO
            return new ArrayList<>();
        }
        return r.body().stream().map(Data::new).collect(Collectors.toList());
    }

    @Override
    @Nonnull
    public List<Contract> getUserContracts() throws IOException {
        final Call<List<ContractModel>> v = connection.getService().getContracts();
        return parseContractCall(v);
    }

    @Override
    public GenericUser getUserFromContract(int contractID) throws IOException {
        Response<reega.data.models.gson.User> r = connection.getService().getUserFromContract(contractID).execute();
        if (r.body() == null) {
            return null;
        }
        return new GenericUser(r.body());
    }

    @Override
    @Nonnull
    public List<Contract> getContractsForUser(String fiscalCode) throws IOException {
        final Call<List<ContractModel>> v = connection.getService().getContractsForUser(fiscalCode);
        return parseContractCall(v);
    }

    @Override
    @Nonnull
    public List<Contract> getAllContracts() throws IOException {
        final Call<List<ContractModel>> v = connection.getService().getAllContracts();
        return parseContractCall(v);
    }

    private List<Contract> parseContractCall(Call<List<ContractModel>> v) throws IOException {
        final Response<List<ContractModel>> r = v.execute();
        if (r.body() == null) {
            return new ArrayList<>();
        }
        return r.body()
                .stream()
                .map(Contract::new)
                .collect(Collectors.toList());
    }

    @Override
    public void addContract(final NewContract contract) throws IOException {
        final Call<Void> v = connection.getService().addContract(contract);
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void removeContract(final int id) throws IOException {
        final Call<Void> v = connection.getService().removeContract(id);
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }
}
