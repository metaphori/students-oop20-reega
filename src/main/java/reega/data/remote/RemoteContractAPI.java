package reega.data.remote;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.ContractController;
import reega.data.models.BaseContract;
import reega.data.models.Contract;
import reega.data.models.DataType;
import reega.data.models.MonthlyReport;
import reega.data.models.gson.ContractModel;
import reega.data.models.gson.MonthlyReportModel;
import reega.data.models.gson.NewContract;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link ContractController} implementation, using remote database via http requests.
 */
public class RemoteContractAPI implements ContractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContractAPI.class);
    private static final int INITIAL_ERROR_CODE = 299;
    private static RemoteConnection connection;

    public RemoteContractAPI(final RemoteConnection c) {
        RemoteContractAPI.connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MonthlyReport> getBillsForContracts(@NotNull final List<Integer> contractIDs) throws IOException {
        RemoteContractAPI.LOGGER.info("getting bill report for contract " + contractIDs);
        final Response<List<MonthlyReportModel>> r = RemoteContractAPI.connection.getService()
                .getBillReport(contractIDs)
                .execute();
        RemoteContractAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteContractAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteContractAPI.LOGGER.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body()
                .stream()
                .collect(Collectors.groupingBy(c -> c.month.getTime()))
                .entrySet()
                .stream()
                .map(entry -> new MonthlyReport(entry.getKey(),
                        entry.getValue()
                                .stream()
                                .collect(Collectors.toMap(e -> DataType.fromId(e.type),
                                        e -> new MonthlyReport.Report(e.sum, e.average))))

                )
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public List<Contract> getUserContracts() throws IOException {
        RemoteContractAPI.LOGGER.info("getting contracts for the user");
        final Response<List<ContractModel>> r = RemoteContractAPI.connection.getService().getContracts().execute();
        return this.parseContractResponse(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public List<Contract> getContractsForUser(final String fiscalCode) throws IOException {
        RemoteContractAPI.LOGGER.info("getting contracts for user with fiscal code " + fiscalCode);
        final Response<List<ContractModel>> r = RemoteContractAPI.connection.getService()
                .getContractsForUser(fiscalCode)
                .execute();
        return this.parseContractResponse(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public List<Contract> getAllContracts() throws IOException {
        RemoteContractAPI.LOGGER.info("getting all the contracts");
        final Response<List<ContractModel>> r = RemoteContractAPI.connection.getService().getAllContracts().execute();
        return this.parseContractResponse(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public List<Contract> searchContract(final String keyword) throws IOException {
        RemoteContractAPI.LOGGER.info("searching for contracts with keyword " + keyword);
        final Response<List<ContractModel>> r = RemoteContractAPI.connection.getService()
                .searchContract(keyword)
                .execute();
        return this.parseContractResponse(r);
    }

    /**
     * Parse the response <code>r</code> to retrieve a list of contracts.
     *
     * @param r response received from the server
     * @return an empty {@link List} if the response is invalid, or the parsed response otherwise
     */
    private List<Contract> parseContractResponse(final Response<List<ContractModel>> r) {
        RemoteContractAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteContractAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteContractAPI.LOGGER.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body().stream().map(BaseContract::new).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contract addContract(final NewContract contract) throws IOException {
        RemoteContractAPI.LOGGER.info("adding contract: " + contract.toString());
        final Response<ContractModel> r = RemoteContractAPI.connection.getService().addContract(contract).execute();
        RemoteContractAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteContractAPI.INITIAL_ERROR_CODE || r.body() == null) {
            RemoteContractAPI.LOGGER.info("error: " + r.errorBody());
            return null;
        }
        return new BaseContract(r.body());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContract(final int id) throws IOException {
        RemoteContractAPI.LOGGER.info("removing contract with ID " + id);
        final Response<Void> r = RemoteContractAPI.connection.getService().removeContract(id).execute();
        RemoteContractAPI.LOGGER.info("response: " + r.code());
        if (r.code() > RemoteContractAPI.INITIAL_ERROR_CODE) {
            RemoteContractAPI.LOGGER.info("error: " + r.errorBody());
        }
    }

}
