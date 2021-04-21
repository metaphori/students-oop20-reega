/**
 *
 */
package reega.main;

import reega.util.ServiceProvider;

/**
 * Interface for the app initializers, to use more than one initializer.
 */
public interface AppInitializer {
    /**
     * Initialize the app.
     *
     */
    void initialize();

    /**
     * Get the service provider for the app.
     *
     * @return the service provider for the app
     */
    ServiceProvider getServiceProvider();
}
