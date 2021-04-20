package reega.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import reega.data.models.DataType;
import reega.data.models.ServiceType;

public class SelectiveUsageSimulator implements UsageSimulator {

    private final Map<DataType, Generator> generators;

    public SelectiveUsageSimulator(final List<DataType> services) {
        this.generators = new HashMap<>();
        services.forEach(srv -> this.generators.put(srv, GaussianGeneratorFactory.getGaussianGenerator(srv)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<DataType, Double> getServicesUsage() {
        return this.getSelectedUsage(List.of(DataType.ELECTRICITY, DataType.GAS, DataType.WATER));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<DataType, Double> getWastesUsage() {
        return this.getSelectedUsage(DataType.getDataTypesByService(ServiceType.GARBAGE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<DataType, Double> getSelectedUsage(final List<DataType> services) {
        return services.stream()
                .filter(this.generators::containsKey)
                .collect(Collectors.toMap(srv -> srv, srv -> this.generators.get(srv).nextValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Double> getUsage(final DataType service) {
        return Optional
                .ofNullable(this.generators.containsKey(service) ? this.generators.get(service).nextValue() : null);
    }

}
