package reega.statistics;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.Data;
import reega.data.models.ServiceType;

public class StatisticsControllerImpl implements StatisticsController {

    private List<Data> data;

    @Override
    public void setData(final List<Data> data) {
        this.data = data;
    }

    @Override
    public Optional<Pair<Date, Double>> getPeek(final ServiceType svcType) {
        return this.groupDataByDay(svcType)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(data -> Pair.of(data.getKey(), data.getValue()));
    }

    @Override
    public double getAverageUsage(final ServiceType svcType) {
        return this.groupDataByDay(svcType).collect(Collectors.averagingDouble(Entry::getValue));
    }

    @Override
    public double getTotalUsage(final ServiceType svcType) {
        return this.groupDataByDay(svcType).collect(Collectors.summingDouble(Entry::getValue));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Data> getCurrentData() {
        return this.data;
    }

    /**
     * Group the data by day
     *
     * @param svcType service type needed for this type of data
     * @return a {@link Stream} containing pairs of Day-Value
     */
    private Stream<Map.Entry<Date, Double>> groupDataByDay(final ServiceType svcType) {
        // If the service type is not GARBAGE then data is produced once a day
        // Else it's produced every hour
        if (svcType != ServiceType.GARBAGE) {
            return this.filterBySvcTypeAndGetData(svcType)
                    .map(elem -> new AbstractMap.SimpleEntry<>(
                            DateUtils.truncate(new Date(elem.getKey()), Calendar.DATE), elem.getValue()));
        }
        return this.filterBySvcTypeAndGetData(svcType)
                .collect(Collectors.groupingBy(elem -> DateUtils.truncate(elem.getKey(), Calendar.DATE),
                        Collectors.summingDouble(Entry::getValue)))
                .entrySet()
                .stream();
    }

    /**
     * Filter data by <code>svcType</code>
     *
     * @param svcType svcType used for filtering data
     * @return a {@link Stream} containing pairs of TimeStamp-Value containing all the data
     */
    private Stream<Map.Entry<Long, Double>> filterBySvcTypeAndGetData(final ServiceType svcType) {
        return this.data.stream()
                .filter(data -> data.getType().getServiceType() == svcType)
                .flatMap(data -> data.getData().entrySet().stream());
    }
}
