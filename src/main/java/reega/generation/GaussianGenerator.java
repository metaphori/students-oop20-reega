package reega.generation;

import java.util.Random;

public class GaussianGenerator implements Generator {

	private final Random rand;
	private final double mean;
	private final double variance;

	public GaussianGenerator(double mean, double variance) {
		this.rand = new Random();
		this.rand.setSeed(this.rand.nextLong());
		this.variance = variance;
		this.mean = mean;
	}

	@Override
	public double nextValue() {
		return mean + variance * rand.nextDouble();
	}

}
