package reega.statistics;

import javafx.scene.chart.XYChart;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.statistics.StatisticsController;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataPlotter {

    /**
     * sets the statistic controller used to gather the data
     *
     * @param statisticsController
     */
    void setStatisticController(StatisticsController statisticsController);

    /**
     * returns data based on the given statistic controller filtered by {@link ServiceType}
     * @param service
     *      {@link ServiceType} to filter by
     * @return
     *      Map of time and usage values
     */
    Map<Date, Double> getData(ServiceType service);
}
