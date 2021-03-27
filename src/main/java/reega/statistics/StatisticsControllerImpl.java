package reega.statistics;

import org.apache.commons.lang3.tuple.Pair;
import reega.data.DataController;
import reega.data.models.ServiceType;

import java.util.Date;
import javax.inject.Inject;

public class StatisticsControllerImpl implements StatisticsController {

    private final DataController dataController;

    @Inject
    public StatisticsControllerImpl(DataController dataController) {
        this.dataController = dataController;
    }


    @Override
    public Pair<Date, Double> getPeek(ServiceType svcType) {
        return null;
    }

    @Override
    public double getAverageUsage(ServiceType svcType) {
        return 0;
    }

    @Override
    public double getTotalUsage(ServiceType svcType) {
        return 0;
    }
}
