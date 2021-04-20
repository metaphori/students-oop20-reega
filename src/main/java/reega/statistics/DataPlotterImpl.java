package reega.statistics;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.ServiceType;

public class DataPlotterImpl implements DataPlotter {

    private StatisticsController statisticsController;
    private ServiceType svcType;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatisticController(final StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, Double> getData() {
        return StatisticsUtils.groupDataByDay(this.statisticsController.getCurrentData(), this.svcType)
                .map(entry -> Pair.of(entry.getKey().getTime(), entry.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServiceType(final ServiceType svcType) {
        this.svcType = svcType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceType getServiceType() {
        return this.svcType;
    }
}
