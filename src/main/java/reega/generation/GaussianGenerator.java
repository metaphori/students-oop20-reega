package reega.generation;

import java.util.Random;

public class GaussianGenerator implements Generator {

    private final Random rand;
    private final double mean;
    private final double variance;

    public GaussianGenerator(final double mean, final double variance) {
        this.rand = new Random();
        this.rand.setSeed(this.rand.nextLong());
        this.variance = variance;
        this.mean = mean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double nextValue() {
        return this.mean + this.variance * this.rand.nextDouble();
    }

}
