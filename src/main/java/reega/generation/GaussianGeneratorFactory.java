package reega.generation;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.ServiceType;

/**
 * static Factory used to create personalized Gaussian Generators
 */
public class GaussianGeneratorFactory {

	static {
		randomizer = new Random();
		rangesMap = Map.ofEntries(
				Map.entry(ServiceType.ELECTRICITY, Pair.of(0.0, 0.0)),
				Map.entry(ServiceType.GAS, Pair.of(0.0, 0.0)),
				Map.entry(ServiceType.WATER, Pair.of(0.0, 0.0)),
				Map.entry(ServiceType.PAPER, Pair.of(0.0, 0.0)),
				Map.entry(ServiceType.GLASS, Pair.of(0.0, 0.0)),
				Map.entry(ServiceType.PLASTIC, Pair.of(0.0, 0.0)),
				Map.entry(ServiceType.MIXED, Pair.of(0.0, 0.0))
				);
	}
	
	private static final Random randomizer;
	/**
	 * map with the value the {@link GaussianGenerator} are initialized with
	 * depend on the service required.
	 * about the Pair: left value = mean, right value = variance. 
	 */
	public static final Map<ServiceType, Pair<Double, Double>> rangesMap;
	//private constructor
	private GaussianGeneratorFactory() {};
	
	/**
	 * creates a {@link GaussianGenerator} with mean and range 
	 * for the specified type of service.
	 * if the specified service does not belong to {@link ServiceType}
	 * a basic {@link Generator} is returned.
	 * 
	 * @param service
	 * 		the type of service the GaussianGenrator will implement
	 * @return
	 */
	public static GaussianGenerator getGaussianGenerator(ServiceType service) {
		GaussianGenerator generator;
		if(rangesMap.containsKey(service)) {
			//mean and variance are now randomized
			double mean = randomizer.nextDouble()*100-50 + rangesMap.get(service).getLeft();
			double variance = randomizer.nextDouble()*10 + rangesMap.get(service).getRight();
			generator = new GaussianGenerator(mean, variance);
		} else {
			generator = new GaussianGenerator(0.0, 0.0);
		}
		return generator;
	}
	
/* alternative to the static Map
 * (getGaussianGenerator would use a switch)
	private static enum ranges {
		ELECTRICITY(0,0),
		GAS(0,0),
		WATER(0,0),
		PAPER(0,0),
		GLASS(0,0),
		PLASTIC(0,0),
		MIXED(0,0);
		
		private final double mean;
		private final double variance;
		
		private ranges(double mean, double variance) {
			this.mean = mean;
			this.variance = variance;
		}

		public double getMean() {
			return mean;
		}

		public double getVariance() {
			return variance;
		}
	}
*/
}
