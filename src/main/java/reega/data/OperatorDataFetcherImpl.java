package reega.data;

import reega.data.models.Data;
import reega.logging.ExceptionHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class OperatorDataFetcherImpl extends DataFetcherImpl implements OperatorDataFetcher {

    @Inject
    public OperatorDataFetcherImpl(DataController contractController, ExceptionHandler exceptionHandler) {
        super(contractController, exceptionHandler);
    }

    @Override
    public List<Data> getGeneralData() {
        try {
            return this.getDataController().getMonthlyData(null);
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e);
            return Collections.emptyList();
        }
    }
}
