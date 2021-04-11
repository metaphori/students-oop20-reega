package reega.data;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ContractFetcherImpl implements ContractFetcher{

    private DataController dataController;
    private ExceptionHandler exceptionHandler;

    public ContractFetcherImpl(DataController dataController, ExceptionHandler exceptionHandler) {
        this.dataController = dataController;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public List<Contract> fetchContractsByUser(User user) {
        try {
            return this.dataController.getUserContracts();
        } catch (IOException e) {
            this.exceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }
}
