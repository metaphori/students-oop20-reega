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

    public SelectiveUsageSimulator(List<DataType> services) {
        this.generators = new HashMap<>();
        services.forEach(srv -> this.generators.put(srv, GaussianGeneratorFactory.getGaussianGenerator(srv)));
    }

    @Override
    public Map<DataType, Double> getServicesUsage() {
        return this.getSelectedUsage(DataType.getDataTypesByService(ServiceType.GARBAGE));
    }

    @Override
    public Map<DataType, Double> getWastesUsage() {
        return this.getSelectedUsage(DataType.getDataTypesByService(ServiceType.GARBAGE));
    }

    @Override
    public Map<DataType, Double> getSelectedUsage(List<DataType> services) {
        return services.stream()
                .filter(generators::containsKey)
                .collect(Collectors.toMap(srv -> srv, srv -> generators.get(srv).nextValue()));
    }

    @Override
    public Optional<Double> getUsage(DataType service) {
        return Optional
                .ofNullable(this.generators.containsKey(service) ? this.generators.get(service).nextValue() : null);
    }

}
