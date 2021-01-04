package reega.generation;

import java.util.Random;

public interface Generator {
	
	/**
	 * generates a random value.
	 * 
	 * @return
	 * 		a new generated value.
	 */
	default public double nextValue() {
		return new Random().nextDouble();
	}
}
