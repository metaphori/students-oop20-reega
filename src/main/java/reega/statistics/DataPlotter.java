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
     * sets the type of data to gather
     *
     * @param svcType {@link ServiceType}
     */
    void setServiceType(ServiceType svcType);

    /**
     * gets the currently set serviceType
     *
     * @return {@link ServiceType}
     */
    ServiceType getServiceType();

    /**
     * returns data based on the given statistic controller filtered by {@link ServiceType}
     *
     * @return Map of time and usage values
     */
    Map<Long, Double> getData();
}
