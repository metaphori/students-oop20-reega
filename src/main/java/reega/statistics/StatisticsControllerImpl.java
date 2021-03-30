package reega.statistics;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

public class StatisticsControllerImpl implements StatisticsController {

    private List<Data> data;


    @Override
    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public Optional<Pair<Date, Double>> getPeek(ServiceType svcType) {
        return groupDataByDay(svcType)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(data -> Pair.of(data.getKey(), data.getValue()));
    }

    @Override
    public double getAverageUsage(ServiceType svcType) {
        return groupDataByDay(svcType).collect(Collectors.averagingDouble(data -> data.getValue()));
    }

    @Override
    public double getTotalUsage(ServiceType svcType) {
        return groupDataByDay(svcType).collect(Collectors.summingDouble(data -> data.getValue()));
    }

    private Stream<Map.Entry<Date,Double>> groupDataByDay(ServiceType svcType) {
        // If the service type is not GARBAGE then data is produced once a day
        // Else it's produced every hour
        if (svcType != ServiceType.GARBAGE) {
            return this.filterBySvcTypeAndGetData(svcType)
                    .map(elem -> new AbstractMap.SimpleEntry<>(DateUtils.truncate(new Date(elem.getKey()), Calendar.DATE),elem.getValue()));
        }
        return this.filterBySvcTypeAndGetData(svcType)
                .collect(Collectors.groupingBy(elem -> DateUtils.truncate(elem.getKey(),Calendar.DATE), Collectors.summingDouble(elem -> elem.getValue())))
                .entrySet()
                .stream();
    }

    private Stream<Map.Entry<Long,Double>> filterBySvcTypeAndGetData(ServiceType svcType) {
        return this.data.stream().filter(data -> data.getType().getServiceType() == svcType)
                .flatMap(data -> data.getData().entrySet().stream());
    }
}
