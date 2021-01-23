package reega.data.remote;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.data.remote.models.ContractModel;
import reega.data.remote.models.DataModel;
import retrofit2.Call;
import retrofit2.Response;

public final class RemoteDatabaseAPI implements DataController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteDatabaseAPI.class);
    private static RemoteConnection connection;
    private static RemoteDatabaseAPI INSTANCE;

    private RemoteDatabaseAPI() {
        connection = new RemoteConnection();
    }

    public synchronized static RemoteDatabaseAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDatabaseAPI();
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
    public List<Contract> getUserContracts(int userID) throws IOException {
        Call<List<ContractModel>> v = connection.getService().getContracts();
        Response<List<ContractModel>> r = v.execute();
        if (r.body() == null) {
            return null;
        }
        return r.body().stream()
                .map(cm -> new Contract(
                        cm.id,
                        cm.userID,
                        cm.address,
                        cm.services,
                        cm.priceModel.getModel(),
                        cm.startTime
                )).collect(Collectors.toList());
    }
}
