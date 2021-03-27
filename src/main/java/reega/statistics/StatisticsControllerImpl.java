package reega.statistics;

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
        return this.data.stream().filter(data -> data.getType().getServiceType() == svcType)
                .flatMap(data -> data.getData().entrySet().stream())
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(data -> Pair.of(new Date(data.getKey()), data.getValue()));
    }

    @Override
    public double getAverageUsage(ServiceType svcType) {
        return filterBySvcTypeAndGetData(svcType).collect(Collectors.averagingDouble(data -> data.getValue()));
    }

    @Override
    public double getTotalUsage(ServiceType svcType) {
        return filterBySvcTypeAndGetData(svcType).collect(Collectors.summingDouble(data -> data.getValue()));
    }

    private Stream<Map.Entry<Long,Double>> filterBySvcTypeAndGetData(ServiceType svcType) {
        return this.data.stream().filter(data -> data.getType().getServiceType() == svcType)
                .flatMap(data -> data.getData().entrySet().stream());
    }
}
