package reega.generation;

import java.util.Map;
import java.util.Optional;

import reega.data.models.ServiceType;


/**
 * simulates the usage of the services for a single contract.
 */
public interface UsageSimulator {
	
	/**
	 * generates the usage of water, electric energy and gas,
	 * but only the ones specified at construction. 
	 * returns an empty map if no services are specified.
	 * 
	 * @return
	 * 		map of utilization values, key is the type of service.
	 */
	public Map<ServiceType, Double> getServicesUsage(); 
	
	/**
	 * generates the values of paper, plastic, glass and mixed wastes,
	 * but only the ones specified at construction.
	 * returns an empty map if no services are specified.
	 * 
	 * @return
	 * 		map of utilization values, key is the type of service.
	 */
	public Map<ServiceType, Double> getWastesUsage();
	
	/**
	 * generates the usage of the specified service; if the service
	 * does not belong to the given contract the method returns an 
	 * null Optional.
	 * 
	 * @param service
	 * 		type of service of which the usage will be generated.
	 * @return
	 * 		an {@link java.util.Optional}.
	 */
	public Optional<Double> getUsage(ServiceType service);
}
