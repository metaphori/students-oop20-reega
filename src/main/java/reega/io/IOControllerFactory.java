/**
 *
 */
package reega.io;

/**
 * @author Marco
 *
 */
public class IOControllerFactory {

    private IOControllerFactory() {

    }

    public static IOController getDefaultIOController() {
        return IOControllerImpl.getInstance();
    }
}
