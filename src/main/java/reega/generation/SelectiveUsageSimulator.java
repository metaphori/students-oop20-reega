package reega.generation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import reega.data.models.ServiceType;

public class SelectiveUsageSimulator implements UsageSimulator {

	private final Map<ServiceType, Generator> generators;

	public SelectiveUsageSimulator(List<ServiceType> services) {
		this.generators = services.stream()
				.collect(Collectors.toMap(srv -> srv, GaussianGeneratorFactory::getGaussianGenerator));
	}

	@Override
	public Map<ServiceType, Double> getServicesUsage() {
		return this.getSelectedUsage(ServiceType.Categories.SERVICES);
	}

	@Override
	public Map<ServiceType, Double> getWastesUsage() {
		return this.getSelectedUsage(ServiceType.Categories.WASTES);
	}

	@Override
	public Map<ServiceType, Double> getSelectedUsage(List<ServiceType> services) {
		return services.stream().filter(generators::containsKey)
				.collect(Collectors.toMap(srv -> srv, srv -> generators.get(srv).nextValue()));
	}

	@Override
	public Optional<Double> getUsage(ServiceType service) {
		return Optional
				.ofNullable(this.generators.containsKey(service) ? this.generators.get(service).nextValue() : null);
	}

}
