package reega.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;

public class OperatorContractManagerImpl extends ContractManagerImpl implements OperatorContractManager {

    @Inject
    public OperatorContractManagerImpl(final ContractController dataController,
            final ExceptionHandler exceptionHandler) {
        super(dataController, exceptionHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contract> fetchContractsByUser(final User user) {
        try {
            return this.getContractController().getContractsForUser(user.getFiscalCode());
        } catch (final IOException e) {
            this.getExceptionHandler().handleException(e);
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUserContract(final Contract contract) {
        try {
            this.getContractController().removeContract(contract.getId());
        } catch (final IOException e) {
            this.getExceptionHandler().handleException(e);
            return false;
        }
        return true;
    }
}
