package reega.data.remote;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.PriceModel;
import reega.data.remote.models.ContractModel;
import reega.data.remote.models.DataModel;
import reega.data.remote.models.NewContract;
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

    public RemoteDatabaseAPI getNewInstance(final RemoteConnection connection) {
        return new RemoteDatabaseAPI(connection);
    }

    @Override
    public void putUserData(final Data data) throws IOException {
        final Call<Void> v = connection.getService().pushData(new DataModel(data));
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
        final Response<List<ContractModel>> r = v.execute();
        if (r.body() == null) {
            return new ArrayList<>();
        }
        return r.body()
                .stream()
                .map(cm -> new Contract(cm.id, cm.address, cm.services, cm.priceModel.getPriceModel(), cm.startTime))
                .collect(Collectors.toList());
    }

    @Override
    @Nonnull
    public List<Contract> getAllContracts() throws IOException {
        final Call<List<ContractModel>> v = connection.getService().getAllContracts();
        final Response<List<ContractModel>> r = v.execute();
        if (r.body() == null) {
            return new ArrayList<>();
        }
        return r.body()
                .stream()
                .map(cm -> new Contract(cm.id, cm.address, cm.services, cm.priceModel.getPriceModel(), cm.startTime))
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

    @Override
    public List<PriceModel> getPriceModels() throws IOException {
        final Call<List<ContractModel.PriceModel>> v = connection.getService().getPriceModels();
        final Response<List<ContractModel.PriceModel>> r = v.execute();
        logger.info(String.valueOf(r.code()));
        if (r.body() == null) {
            return null;
        }
        return r.body().stream().map(ContractModel.PriceModel::getPriceModel).collect(Collectors.toList());
    }

    @Override
    public void addPriceModel(final PriceModel priceModel) throws IOException {
        final ContractModel.PriceModel model = new ContractModel.PriceModel();
        model.name = priceModel.getName();
        model.prices = priceModel.getPrices();
        final Call<Void> v = connection.getService().addPriceModel(model);
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void removePriceModel(final int id) throws IOException {
        final Call<Void> v = connection.getService().removePriceModel(id);
        final Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }
}
