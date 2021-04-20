package reega.statistics;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;

import reega.data.models.Data;
import reega.data.models.ServiceType;

public final class StatisticsUtils {

    private StatisticsUtils() {
    }

    /**
     * Group the data by day.
     *
     * @param data    data that needs to be grouped
     * @param svcType service type needed for this type of data
     * @return a {@link Stream} containing pairs of Day-Value
     */
    public static Stream<Map.Entry<Date, Double>> groupDataByDay(final List<Data> data, final ServiceType svcType) {
        // If the service type is not GARBAGE then data is produced once a day
        // Else it's produced every hour
        return StatisticsUtils.filterBySvcTypeAndGetData(data, svcType)
                .collect(Collectors.groupingBy(elem -> DateUtils.truncate(new Date(elem.getKey()), Calendar.DATE),
                        Collectors.summingDouble(Entry::getValue)))
                .entrySet()
                .stream();
    }

    /**
     * Filter data by <code>svcType</code>.
     *
     * @param data    data that needs to be filtered
     * @param svcType svcType used for filtering data
     * @return a {@link Stream} containing pairs of TimeStamp-Value containing all the data
     */
    public static Stream<Map.Entry<Long, Double>> filterBySvcTypeAndGetData(final List<Data> data,
            final ServiceType svcType) {
        return data.stream()
                .filter(dat -> dat.getType().getServiceType() == svcType)
                .flatMap(dat -> dat.getData().entrySet().stream());
    }
}
