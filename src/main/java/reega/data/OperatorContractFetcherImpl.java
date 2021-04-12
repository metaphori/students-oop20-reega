package reega.data;

import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OperatorContractFetcherImpl extends ContractFetcherImpl implements OperatorContractFetcher{

    @Inject
    public OperatorContractFetcherImpl(DataController dataController, ExceptionHandler exceptionHandler) {
        super(dataController, exceptionHandler);
    }

    @Override
    public List<Contract> fetchContractsByUser(User user) {
        try {
            return this.getDataController().getContractsForUser(user.getFiscalCode());
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e);
            return Collections.emptyList();
        }
    }
}
