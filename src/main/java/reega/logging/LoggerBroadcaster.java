/**
 *
 */
package reega.logging;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author Marco Broadcaster of a logger
 */
public interface LoggerBroadcaster {
    /**
     * Append a logger to the list of loggers
     *
     * @param logger logger to append
     */
    void appendLogger(Logger logger);

    /**
     * Get all the loggers
     *
     * @return a collection of loggers
     */
    Collection<Logger> getLoggers();

    /**
     * Remove a logger from the collection of loggers it is present
     *
     * @param logger logger to remove
     */
    void removeLogger(Logger logger);

    /**
     * Broadcast a simple message
     *
     * @param message message to broadcast
     */
    void broadcastLog(final String message);

    /**
     * Broadcast an error message
     *
     * @param errorMessage error message to broadcast
     */
    void broadcastError(final String errorMessage);

    /**
     * Invoke {@code actionToPerform} to every logger in {@link #loggers}
     *
     * @param actionToPerform action to perform having a logger
     */
    void broadcastToLoggers(final Consumer<Logger> actionToPerform);
}
