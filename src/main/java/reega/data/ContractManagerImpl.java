package reega.data;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ContractManagerImpl implements ContractManager {

    private ContractController contractController;
    private ExceptionHandler exceptionHandler;

    @Inject
    public ContractManagerImpl(ContractController contractController, ExceptionHandler exceptionHandler) {
        this.contractController = contractController;
        this.exceptionHandler = exceptionHandler;
    }

    protected final ContractController getContractController() {
        return this.contractController;
    }

    protected final ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    @Override
    public List<Contract> fetchUserContracts() {
        try {
            return this.contractController.getUserContracts();
        } catch (IOException e) {
            this.exceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }
}
