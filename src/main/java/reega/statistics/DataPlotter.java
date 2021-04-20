package reega.statistics;

import java.util.Map;

import reega.data.models.ServiceType;

public interface DataPlotter {

    /**
     * Set the statistic controller used to gather the data.
     *
     * @param statisticsController statistics controller
     */
    void setStatisticController(StatisticsController statisticsController);

    /**
     * Set the type of data to gather.
     *
     * @param svcType {@link ServiceType}
     */
    void setServiceType(ServiceType svcType);

    /**
     * Get the currently set serviceType.
     *
     * @return {@link ServiceType}
     */
    ServiceType getServiceType();

    /**
     * Return data based on the given statistic controller filtered by {@link ServiceType}.
     *
     * @return Map of time and usage values
     */
    Map<Long, Double> getData();
}
