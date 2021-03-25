package reega.data;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

public interface StatisticsController {
    /**
     * Get the peek usage
     * @return the date of the peek usage and the value of the peek
     */
    Pair<Date,Double> getPeek();

    /**
     * Get the average usage
     * @return the average usage
     */
    double getAverageUsage();

    /**
     * Get the total usage
     * @return the total usage
     */
    double getTotalUsage();
}
