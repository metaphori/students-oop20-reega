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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final List<Data> data) {
        this.data = data;
        this.dataCacheMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Pair<Date, Double>> getPeek(final ServiceType svcType) {
        final DataCache dataCache = this.getOrCreateDataCache(svcType);
        if (dataCache.getPeek().isPresent()) {
            return dataCache.getPeek();
        }
        final Optional<Pair<Date, Double>> peekValue = StatisticsUtils.groupDataByDay(this.data, svcType)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(data -> Pair.of(data.getKey(), data.getValue()));
        dataCache.setPeek(peekValue);
        return dataCache.getPeek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAverageUsage(final ServiceType svcType) {
        final DataCache dataCache = this.getOrCreateDataCache(svcType);
        if (dataCache.getAverageUsage().isPresent()) {
            return dataCache.getAverageUsage().get();
        }
        final double averageUsage = StatisticsUtils.groupDataByDay(this.data, svcType)
                .collect(Collectors.averagingDouble(Entry::getValue));
        dataCache.setAverageUsage(averageUsage);
        return dataCache.getAverageUsage().get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTotalUsage(final ServiceType svcType) {
        final DataCache dataCache = this.getOrCreateDataCache(svcType);
        if (dataCache.getTotalUsage().isPresent()) {
            return dataCache.getTotalUsage().get();
        }
        final double totalUsage = StatisticsUtils.filterBySvcTypeAndGetData(this.data, svcType)
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
     * {@link #dataCacheMap}.
     *
     * @param svcType {@link ServiceType} needed
     * @return the {@link DataCache} in the map
     */
    private DataCache getOrCreateDataCache(final ServiceType svcType) {
        if (this.dataCacheMap.containsKey(svcType)) {
            return this.dataCacheMap.get(svcType);
        }
        final DataCache dCache = new DataCache();
        this.dataCacheMap.put(svcType, dCache);
        return dCache;
    }

    /**
     * Caching class for the data in order to perform the operation only once.
     */
    private static class DataCache {
        private Optional<Pair<Date, Double>> peek = Optional.empty();
        private Optional<Double> averageUsage = Optional.empty();
        private Optional<Double> totalUsage = Optional.empty();

        /**
         * Set the peek value.
         *
         * @param peek peek value
         */
        public void setPeek(final Optional<Pair<Date, Double>> peek) {
            this.peek = peek;
        }

        /**
         * Set the average usage.
         *
         * @param averageUsage average usage
         */
        public void setAverageUsage(final double averageUsage) {
            this.averageUsage = Optional.of(averageUsage);
        }

        /**
         * Set the total usage.
         *
         * @param totalUsage total usage
         */
        public void setTotalUsage(final double totalUsage) {
            this.totalUsage = Optional.of(totalUsage);
        }

        /**
         * Get the peek value.
         *
         * @return the peek value
         */
        public Optional<Pair<Date, Double>> getPeek() {
            return this.peek;
        }

        /**
         * Get the average usage.
         *
         * @return the average usage
         */
        public Optional<Double> getAverageUsage() {
            return this.averageUsage;
        }

        /**
         * Get the total usage.
         *
         * @return the total usage
         */
        public Optional<Double> getTotalUsage() {
            return this.totalUsage;
        }
    }
}
