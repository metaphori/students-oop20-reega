package reega.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;

public class ContractManagerImpl implements ContractManager {

    private final ContractController contractController;
    private final ExceptionHandler exceptionHandler;

    @Inject
    public ContractManagerImpl(final ContractController contractController, final ExceptionHandler exceptionHandler) {
        this.contractController = contractController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Get the contract controller.
     *
     * @return the contract controller
     */
    protected final ContractController getContractController() {
        return this.contractController;
    }

    /**
     * Get the exception handler.
     *
     * @return the exception handler
     */
    protected final ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contract> fetchUserContracts() {
        try {
            return this.contractController.getUserContracts();
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }
}
