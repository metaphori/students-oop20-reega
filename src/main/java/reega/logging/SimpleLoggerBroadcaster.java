/**
 *
 */
package reega.logging;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Marco
 *
 */
public class SimpleLoggerBroadcaster implements LoggerBroadcaster {

    private final Collection<Logger> loggers = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendLogger(final Logger logger) {
        this.loggers.add(logger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Logger> getLoggers() {
        return Set.copyOf(this.loggers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLogger(final Logger logger) {
        this.loggers.remove(logger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void broadcastLog(final String message) {
        this.broadcastToLoggers(logger -> logger.log(message));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void broadcastError(final String errorMessage) {
        this.broadcastToLoggers(logger -> logger.logError(errorMessage));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void broadcastToLoggers(final Consumer<Logger> actionToPerform) {
        for (final Logger logger : this.loggers) {
            actionToPerform.accept(logger);
        }
    }

}
