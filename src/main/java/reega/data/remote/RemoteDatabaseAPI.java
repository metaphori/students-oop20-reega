package reega.data.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.PriceModel;
import reega.data.models.ServiceType;
import reega.data.remote.models.ContractModel;
import reega.data.remote.models.DataModel;
import reega.data.remote.models.NewContract;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DataController implementation, using remote database via http requests
 */
public class RemoteDatabaseAPI implements DataController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteDatabaseAPI.class);
    private static RemoteConnection connection;
    private static RemoteDatabaseAPI INSTANCE;

    private RemoteDatabaseAPI(RemoteConnection c) {
        connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    public synchronized static RemoteDatabaseAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDatabaseAPI(null);
        }

        return INSTANCE;
    }

    public static synchronized RemoteDatabaseAPI getInstanceWithConnection(RemoteConnection conn) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDatabaseAPI(conn);
        }
        return INSTANCE;
    }

    @Override
    public void putUserData(Data data) throws IOException {
        Call<Void> v = connection.getService()
                .pushData(new DataModel(data));
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public Long getLatestData(int contractID, ServiceType service) throws IOException {
        Call<Date> v = connection.getService().getLatestData(service.getID(), contractID);
        Response<Date> r = v.execute();
        Date d = r.body();
        return d == null ? 0L : d.getTime();
    }

    @Override
    @Nonnull
    public List<Contract> getUserContracts() throws IOException {
        Call<List<ContractModel>> v = connection.getService().getContracts();
        Response<List<ContractModel>> r = v.execute();
        if (r.body() == null) {
            return new ArrayList<>();
        }
        return r.body().stream()
                .map(cm -> new Contract(
                        cm.id,
                        cm.address,
                        cm.services,
                        cm.priceModel.getPriceModel(),
                        cm.startTime
                )).collect(Collectors.toList());
    }

    @Override
    public void addContract(NewContract contract) throws IOException {
        Call<Void> v = connection.getService().addContract(contract);
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void removeContract(int id) throws IOException {
        Call<Void> v = connection.getService().removeContract(id);
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public List<PriceModel> getPriceModels() throws IOException {
        Call<List<ContractModel.PriceModel>> v = connection.getService().getPriceModels();
        Response<List<ContractModel.PriceModel>> r = v.execute();
        logger.info(String.valueOf(r.code()));
        if (r.body() == null) {
            return null;
        }
        return r.body().stream().map(ContractModel.PriceModel::getPriceModel).collect(Collectors.toList());
    }

    @Override
    public void addPriceModel(PriceModel priceModel) throws IOException {
        ContractModel.PriceModel model = new ContractModel.PriceModel();
        model.name = priceModel.getName();
        model.prices = priceModel.getPrices();
        Call<Void> v = connection.getService().addPriceModel(model);
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }

    @Override
    public void removePriceModel(int id) throws IOException {
        Call<Void> v = connection.getService().removePriceModel(id);
        Response<Void> r = v.execute();
        logger.info(String.valueOf(r.code()));
        // TODO check successful state
    }
}
