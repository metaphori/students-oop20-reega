package reega.generation;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.DataType;

/**
 * static Factory used to create personalized Gaussian Generators
 */
public class GaussianGeneratorFactory {

    static {
        randomizer = new Random();
        rangesMap = Map.ofEntries(Map.entry(DataType.ELECTRICITY, Pair.of(0.0, 0.0)),
                Map.entry(DataType.GAS, Pair.of(0.0, 0.0)), Map.entry(DataType.WATER, Pair.of(0.0, 0.0)),
                Map.entry(DataType.PAPER, Pair.of(0.0, 0.0)), Map.entry(DataType.GLASS, Pair.of(0.0, 0.0)),
                Map.entry(DataType.PLASTIC, Pair.of(0.0, 0.0)), Map.entry(DataType.MIXED, Pair.of(0.0, 0.0)));
    }

    private static final Random randomizer;
    /*
     * map with the values used to initialize the gaussianGnerators depending on the service required. Pair: left value
     * = mean, right value = variance.
     */
    public static final Map<DataType, Pair<Double, Double>> rangesMap;

    // private constructor
    private GaussianGeneratorFactory() {
    };

    /**
     * creates a {@link GaussianGenerator} with mean and range for the specified type of service. if the specified
     * service does not belong to {@link DataType} a basic {@link Generator} is returned.
     *
     * @param service the type of service the GaussianGenrator will generate data for.
     * @return
     */
    public static GaussianGenerator getGaussianGenerator(DataType service) {
        GaussianGenerator generator;
        if (rangesMap.containsKey(service)) {
            // mean and variance are now randomized
            double mean = randomizer.nextDouble() * 100 + rangesMap.get(service).getLeft();
            double variance = randomizer.nextDouble() * 10 + rangesMap.get(service).getRight();
            generator = new GaussianGenerator(mean, variance) {
                @Override
                public double nextValue() {
                    return Math.abs(super.nextValue());
                }
            };
        } else {
            generator = new GaussianGenerator(0.0, 0.0);
        }
        return generator;
    }

}
