package reega.data.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.DataController;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.gson.DataModel;
import retrofit2.Response;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RemoteDataAPI implements DataController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteContractAPI.class);
    private static RemoteConnection connection;

    public RemoteDataAPI(final RemoteConnection c) {
        connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    @Override
    public void putUserData(final Data data) throws IOException {
        logger.info("inserting data for contract ID: " + data.getContractID());
        final Response<Void> r = connection.getService().pushData(data.getJsonModel()).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299) {
            logger.info("error: " + r.errorBody());
        }
    }

    @Override
    public Long getLatestData(final int contractID, final DataType service) throws IOException {
        logger.info("getting latest timestamp for contract ID " + contractID +
                " and service " + service.getServiceType().getName());
        final Response<Date> r = connection.getService().getLatestData(service.getID(), contractID).execute();

        logger.info("response: " + r.code());
        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return 0L;
        }
        final Date d = r.body();
        return d.getTime();
    }

    @Override
    public List<Data> getMonthlyData(@Nullable final Integer contractID) throws IOException {
        final Map<String, String> options = new HashMap<>();
        if (contractID != null) {
            logger.info("getting global monthly data");
            options.put("contract_id", String.valueOf(contractID));
        } else {
            logger.info("getting monthly data related to contract ID " + contractID);
        }
        final Response<List<DataModel>> r = connection.getService().getMonthlyData(options).execute();
        logger.info("response: " + r.code());

        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body().stream().map(Data::new).collect(Collectors.toList());
    }
}
