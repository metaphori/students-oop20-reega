package reega.generation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import reega.data.models.ServiceType;


/**
 * simulates the usage of the services specified at construction.
 */
public interface UsageSimulator {
	
	/**
	 * generates the usage for water, electric energy and gas,
	 * excluding the ones NOT specified at construction.
	 * returns an empty map if no services are specified.
	 * 
	 * @return
	 * 		map of utilization values, key is the type of service.
	 */
	public Map<ServiceType, Double> getServicesUsage(); 
	
	/**
	 * generates the values for paper, plastic, glass and mixed wastes,
	 * excluding the ones NOT specified at construction.
	 * returns an empty map if no services are specified.
	 * 
	 * @return
	 * 		map of utilization values, key is the type of service.
	 */
	public Map<ServiceType, Double> getWastesUsage();
	
	/**
	 * generates the values for selected services
	 * excluding the ones NOT specified at construction.
	 * returns an empty map if no services are specified or selected.
	 * 
	 * @return
	 * 		map of utilization values, key is the type of service.
	 */
	public Map<ServiceType, Double> getSelectedUsage(List<ServiceType> services);

	/**
	 * generates the usage for the specified service; if the service
	 * does not belong to the ones specifies ate construction 
	 * the method returns an Optional null.
	 * 
	 * @param service
	 * 		type of service of which the usage will be generated.
	 * @return
	 * 		an {@link java.util.Optional}.
	 */
	public Optional<Double> getUsage(ServiceType service);
}
