package reega.statistics;

import java.util.Map;

import reega.data.models.ServiceType;

public interface DataPlotter {

    /**
     * sets the statistic controller used to gather the data
     *
     * @param statisticsController
     */
    void setStatisticController(StatisticsController statisticsController);

    /**
     * returns data based on the given statistic controller filtered by {@link ServiceType}
     *
     * @param svcType {@link ServiceType} to filter by
     * @return Map of time and usage values
     */
    Map<Long, Double> getData(ServiceType svcType);
}
