package reega.data.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.data.DataController;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.gson.DataModel;
import retrofit2.Response;

/**
 * {@link DataController} implementation, using remote database via http requests.
 */
public class RemoteDataAPI implements DataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContractAPI.class);
    private static final int INITIAL_ERROR_CODE = 299;
    private static RemoteConnection connection;

    public RemoteDataAPI(final RemoteConnection c) {
        RemoteDataAPI.connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putUserData(final Data data) throws IOException {
        RemoteDataAPI.LOGGER.info("inserting data for contract ID: " + data.getContractID());
        final Response<Void> r = RemoteDataAPI.connection.getService().pushData(data.getJsonModel()).execute();
        RemoteDataAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteDataAPI.INITIAL_ERROR_CODE) {
            RemoteDataAPI.LOGGER.info("error: " + r.errorBody());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLatestData(final int contractID, final DataType service) throws IOException {
        RemoteDataAPI.LOGGER.info("getting latest timestamp for contract ID " + contractID + " and service "
                + service.getServiceType().getName());
        final Response<Date> r = RemoteDataAPI.connection.getService()
                .getLatestData(service.getID(), contractID)
                .execute();

        RemoteDataAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteDataAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteDataAPI.LOGGER.info("error: " + r.errorBody());
            return 0L;
        }
        final Date d = r.body();
        return d.getTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Data> getMonthlyData(@Nullable final Integer contractID) throws IOException {
        final Map<String, String> options = new HashMap<>();
        if (contractID != null) {
            RemoteDataAPI.LOGGER.info("getting global monthly data");
            options.put("contract_id", String.valueOf(contractID));
        } else {
            RemoteDataAPI.LOGGER.info("getting monthly data related to contract ID " + contractID);
        }
        final Response<List<DataModel>> r = RemoteDataAPI.connection.getService().getMonthlyData(options).execute();
        RemoteDataAPI.LOGGER.info("response: " + r.code());

        if (r.code() > RemoteDataAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteDataAPI.LOGGER.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body().stream().map(Data::new).collect(Collectors.toList());
    }
}
