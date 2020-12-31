/**
 *
 */
package reega.logging;

/**
 * @author Marco Logger that logs to the stdout of the process
 */
public class ConsoleLogger implements Logger {

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(final String message) {
        System.out.println(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logError(final String message) {
        System.err.println(message);
    }

}
