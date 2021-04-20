package reega.util;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class for retrieving services stored in a {@link ServiceCollection}.
 *
 */
public class ServiceProvider {

    private final ServiceCollection svcCollection;

    ServiceProvider(final ServiceCollection svcCollection) {
        this.svcCollection = svcCollection;
    }

    /**
     * Get a service or an empty optional if no service has been found.
     *
     * @param <T>  Type of the service
     * @param type Class type
     * @return a Optional filled in with the service if the service exists, an empty Optional otherwise
     */
    public <T> Optional<T> getService(final Class<T> type) {
        return this.svcCollection.getService(type);
    }

    /**
     * Get a service.
     *
     * @param <T>  Type of the service
     * @param type Class type
     * @return an Instance of the service
     * @throws NoSuchElementException if the service hasn't been found
     */
    public <T> T getRequiredService(final Class<T> type) {
        final Optional<T> service = this.getService(type);
        return service.orElseThrow(NoSuchElementException::new);
    }

}
