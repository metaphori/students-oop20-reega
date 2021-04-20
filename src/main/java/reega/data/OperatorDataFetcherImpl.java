package reega.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import reega.data.models.Data;
import reega.logging.ExceptionHandler;

public class OperatorDataFetcherImpl extends DataFetcherImpl implements OperatorDataFetcher {

    @Inject
    public OperatorDataFetcherImpl(final DataController contractController, final ExceptionHandler exceptionHandler) {
        super(contractController, exceptionHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Data> getGeneralData() {
        try {
            return this.getDataController().getMonthlyData(null);
        } catch (final IOException e) {
            this.getExceptionHandler().handleException(e);
            return Collections.emptyList();
        }
    }
}
