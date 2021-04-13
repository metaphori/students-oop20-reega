package reega.data;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OperatorContractManagerImpl extends ContractManagerImpl implements OperatorContractManager {

    @Inject
    public OperatorContractManagerImpl(ContractController dataController, ExceptionHandler exceptionHandler) {
        super(dataController, exceptionHandler);
    }

    @Override
    public List<Contract> fetchContractsByUser(User user) {
        try {
            return this.getContractController().getContractsForUser(user.getFiscalCode());
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean deleteUserContract(Contract contract) {
        try {
            this.getContractController().removeContract(contract.getId());
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e);
            return false;
        }
        return true;
    }
}
