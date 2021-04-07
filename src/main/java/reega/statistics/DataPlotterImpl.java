package reega.statistics;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.ServiceType;

public class DataPlotterImpl implements DataPlotter {

    private StatisticsController statisticsController;

    @Override
    public void setStatisticController(StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }

    @Override
    public Map<Long, Double> getData(ServiceType svcType) {
        return StatisticsUtils.groupDataByDay(this.statisticsController.getCurrentData(), svcType)
                .map(entry -> Pair.of(entry.getKey().getTime(), entry.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
