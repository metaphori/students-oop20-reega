/**
 *
 */
package reega.main;

import reega.util.ServiceProvider;

/**
 * Interface for the app initializers, to use more than one initializer
 *
 * @author Marco
 *
 */
public interface AppInitializer {
    /**
     * Initialize the app
     *
     * @throws Exception throw a generic exception that can be raised when
     *                   initializing everything
     */
    void initialize() throws Exception;

    /**
     * Get the service provider for the app
     *
     * @return the service provider for the app
     */
    ServiceProvider getServiceProvider();
}
