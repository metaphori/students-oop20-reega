package reega.data;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.logging.ExceptionHandler;
import reega.users.User;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataFetcherImpl implements DataFetcher {

    private Map<Contract, List<Data>> currentDataByContract;
    private DataController contractController;
    private ExceptionHandler exceptionHandler;

    @Inject
    public DataFetcherImpl(DataController contractController, ExceptionHandler exceptionHandler) {
        this.contractController = contractController;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public List<Data> fetchAllUserData(User user, List<Contract> contracts) {
        this.currentDataByContract = new HashMap<>();
        return contracts.stream().flatMap(contract -> {
            final List<Data> monthlyData = this.getDataByContract(contract);
            this.currentDataByContract.put(contract, monthlyData);
            return monthlyData.stream();
        }).collect(Collectors.toList());
    }

    @Override
    public List<Data> pushAndFetchContract(List<Data> oldData, Contract contract) {
        final List<Data> monthlyData = this.getDataByContract(contract);
        final Stream<Data> newDataStream = monthlyData.stream();
        final List<Data> allData = Stream
                .concat(newDataStream, oldData.stream())
                .collect(Collectors.toList());
        this.currentDataByContract.put(contract, monthlyData);
        return allData;
    }

    @Override
    public List<Data> removeAndFetchContract(List<Data> oldData, Contract contract) {
        this.currentDataByContract.remove(contract);
        return this.currentDataByContract.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Get the data controller
     *
     * @return the data controller
     */
    protected final DataController getDataController() {
        return this.contractController;
    }

    /**
     * Get the exception handler
     *
     * @return the exception handler
     */
    protected final ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    /**
     * Get the data by a contract
     *
     * @param contract contract to search
     * @return a list of data containing data for the contract
     */
    protected final List<Data> getDataByContract(final Contract contract) {
        try {
            return this.contractController.getMonthlyData(contract.getId());
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "Failed to load data for the contract: " + contract.getId());
        }
        return Collections.emptyList();
    }
}
