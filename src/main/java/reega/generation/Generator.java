package reega.generation;

import java.util.Random;

public interface Generator {

    /**
     * Generates a random value.
     *
     * @return a new generated value.
     */
    default double nextValue() {
        return new Random().nextDouble();
    }
}
