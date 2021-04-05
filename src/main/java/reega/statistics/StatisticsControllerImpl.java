package reega.statistics;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.Data;
import reega.data.models.ServiceType;

public class StatisticsControllerImpl implements StatisticsController {

    private List<Data> data;
    private Map<ServiceType, DataCache> dataCacheMap;

    @Override
    public void setData(final List<Data> data) {
        this.data = data;
        this.dataCacheMap = new HashMap<>();
    }

    @Override
    public Optional<Pair<Date, Double>> getPeek(final ServiceType svcType) {
        DataCache dataCache = this.getOrCreateDataCache(svcType);
        if (dataCache.getPeek().isPresent()) {
            return dataCache.getPeek();
        }
        Optional<Pair<Date, Double>> peekValue = StatisticsUtils.groupDataByDay(this.data, svcType)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(data -> Pair.of(data.getKey(), data.getValue()));
        dataCache.setPeek(peekValue);
        return dataCache.getPeek();
    }

    @Override
    public double getAverageUsage(final ServiceType svcType) {
        DataCache dataCache = this.getOrCreateDataCache(svcType);
        if (dataCache.getAverageUsage().isPresent()) {
            return dataCache.getAverageUsage().get();
        }
        double averageUsage = StatisticsUtils.groupDataByDay(this.data, svcType)
                .collect(Collectors.averagingDouble(Entry::getValue));
        dataCache.setAverageUsage(averageUsage);
        return dataCache.getAverageUsage().get();
    }

    @Override
    public double getTotalUsage(final ServiceType svcType) {
        DataCache dataCache = this.getOrCreateDataCache(svcType);
        if (dataCache.getTotalUsage().isPresent()) {
            return dataCache.getTotalUsage().get();
        }
        double totalUsage = StatisticsUtils.filterBySvcTypeAndGetData(this.data, svcType)
                .collect(Collectors.summingDouble(Entry::getValue));
        dataCache.setTotalUsage(totalUsage);
        return dataCache.getTotalUsage().get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Data> getCurrentData() {
        return this.data;
    }

    /**
     * Get the {@link DataCache} object corresponding to the <code>svcType</code> or create it and store it into the
     * {@link #dataCacheMap}
     *
     * @param svcType {@link ServiceType} needed
     * @return the {@link DataCache} in the map
     */
    private DataCache getOrCreateDataCache(ServiceType svcType) {
        if (this.dataCacheMap.containsKey(svcType)) {
            return this.dataCacheMap.get(svcType);
        }
        DataCache dCache = new DataCache();
        this.dataCacheMap.put(svcType, dCache);
        return dCache;
    }

    /**
     * Caching class for the data in order to perform the operation only once
     */
    private static class DataCache {
        private Optional<Pair<Date, Double>> peek = Optional.empty();
        private Optional<Double> averageUsage = Optional.empty();
        private Optional<Double> totalUsage = Optional.empty();

        /**
         * Set the peek value
         *
         * @param peek peek value
         */
        public void setPeek(Optional<Pair<Date, Double>> peek) {
            this.peek = peek;
        }

        /**
         * Set the average usage
         *
         * @param averageUsage average usage
         */
        public void setAverageUsage(double averageUsage) {
            this.averageUsage = Optional.of(averageUsage);
        }

        /**
         * Set the total usage
         *
         * @param totalUsage total usage
         */
        public void setTotalUsage(double totalUsage) {
            this.totalUsage = Optional.of(totalUsage);
        }

        /**
         * Get the peek value
         *
         * @return the peek value
         */
        public Optional<Pair<Date, Double>> getPeek() {
            return this.peek;
        }

        /**
         * Get the average usage
         *
         * @return the average usage
         */
        public Optional<Double> getAverageUsage() {
            return this.averageUsage;
        }

        /**
         * Get the total usage
         *
         * @return the total usage
         */
        public Optional<Double> getTotalUsage() {
            return totalUsage;
        }
    }
}
