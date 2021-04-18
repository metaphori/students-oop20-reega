package reega.data.remote;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.ContractController;
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
 * DataController implementation, using remote database via http requests
 */
public class RemoteContractAPI implements ContractController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteContractAPI.class);
    private static RemoteConnection connection;

    public RemoteContractAPI(final RemoteConnection c) {
        connection = Objects.requireNonNullElseGet(c, RemoteConnection::new);
    }

    @Override
    public List<MonthlyReport> getBillsForContracts(@NotNull List<Integer> contractIDs) throws IOException {
        logger.info("getting bill report for contract " + contractIDs);
        final Response<List<MonthlyReportModel>> r = connection.getService().getBillReport(contractIDs).execute();
        logger.info("response: "+r.code());
        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body().stream().collect(Collectors.groupingBy(c -> c.month.getTime()))
                .entrySet().stream().map(
                        entry -> new MonthlyReport(entry.getKey(),
                                entry.getValue().stream().collect(Collectors.toMap(
                                        e -> DataType.fromId(e.type),
                                        e -> new MonthlyReport.Report(e.sum, e.average)
                                        )
                                )
                        )

                ).collect(Collectors.toList());
    }

    @Override
    @Nonnull
    public List<Contract> getUserContracts() throws IOException {
        logger.info("getting contracts for the user");
        final Response<List<ContractModel>> r = connection.getService().getContracts().execute();
        return parseContractResponse(r);
    }

    @Override
    @Nonnull
    public List<Contract> getContractsForUser(final String fiscalCode) throws IOException {
        logger.info("getting contracts for user with fiscal code " + fiscalCode);
        final Response<List<ContractModel>> r = connection.getService().getContractsForUser(fiscalCode).execute();
        return parseContractResponse(r);
    }

    @Override
    @Nonnull
    public List<Contract> getAllContracts() throws IOException {
        logger.info("getting all the contracts");
        final Response<List<ContractModel>> r = connection.getService().getAllContracts().execute();
        return parseContractResponse(r);
    }

    @Override
    @Nonnull
    public List<Contract> searchContract(String keyword) throws IOException {
        logger.info("searching for contracts with keyword " + keyword);
        final Response<List<ContractModel>> r = connection.getService().searchContract(keyword).execute();
        return parseContractResponse(r);
    }

    private List<Contract> parseContractResponse(final Response<List<ContractModel>> r) {
        logger.info("response: " + r.code());
        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return new ArrayList<>();
        }
        return r.body()
                .stream()
                .map(Contract::new)
                .collect(Collectors.toList());
    }

    @Override
    public Contract addContract(final NewContract contract) throws IOException {
        logger.info("adding contract: " + contract.toString());
        final Response<ContractModel> r = connection.getService().addContract(contract).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299 || r.body() == null) {
            logger.info("error: " + r.errorBody());
            return null;
        }
        return new Contract(r.body());
    }

    @Override
    public void removeContract(final int id) throws IOException {
        logger.info("removing contract with ID " + id);
        final Response<Void> r = connection.getService().removeContract(id).execute();
        logger.info("response: " + r.code());
        if (r.code() > 299) {
            logger.info("error: " + r.errorBody());
        }
    }


}
