package reega.data;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ContractFetcherImpl implements ContractFetcher{

    private ContractController contractController;
    private ExceptionHandler exceptionHandler;

    @Inject
    public ContractFetcherImpl(ContractController contractController, ExceptionHandler exceptionHandler) {
        this.contractController = contractController;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public List<Contract> fetchContractsByUser(User user) {
        try {
            return this.contractController.getUserContracts();
        } catch (IOException e) {
            this.exceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }
}
