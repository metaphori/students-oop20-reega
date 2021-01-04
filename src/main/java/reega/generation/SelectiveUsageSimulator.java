package reega.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import reega.data.models.ServiceType;

public class SelectiveUsageSimulator implements UsageSimulator {

	static {
		WASTES = List.of(ServiceType.PAPER, ServiceType.PLASTIC, ServiceType.GLASS, ServiceType.MIXED);
		SERVICES = List.of(ServiceType.WATER, ServiceType.ELECTRICITY, ServiceType.GAS);
	}

	private static final List<ServiceType> WASTES;
	private static final List<ServiceType> SERVICES;
	private final Map<ServiceType, Generator> generators;
	
	public SelectiveUsageSimulator(List<ServiceType> services) {
		this.generators = new HashMap<>(services.size(), 1.0001f);//the HashMap will not grow hence load factor = 1
		services.forEach(srv -> this.generators.put(srv, GaussianGeneratorFactory.getGaussianGenerator(srv)));
	}

	@Override
	public Map<ServiceType, Double> getServicesUsage() {
		return this.getSelectedUsage(SERVICES);
	}

	@Override
	public Map<ServiceType, Double> getWastesUsage() {
		return this.getSelectedUsage(WASTES);
	}

	@Override
	public Map<ServiceType, Double> getSelectedUsage(List<ServiceType> services) {
		return services.stream()
				.filter(generators::containsKey)
				.collect(Collectors.toMap(srv -> srv, srv -> generators.get(srv).nextValue()));
	}

	@Override
	public Optional<Double> getUsage(ServiceType service) {
		return Optional.ofNullable(
				this.generators.containsKey(service) ? this.generators.get(service).nextValue() : null
						);
	}

}
