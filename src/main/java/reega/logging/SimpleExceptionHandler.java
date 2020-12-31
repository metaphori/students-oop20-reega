/**
 *
 */
package reega.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author Marco
 *
 */
public class SimpleExceptionHandler implements ExceptionHandler {

    private final LoggerBroadcaster broadcaster;

    /**
     * @param broadcaster broadcaster used for logging
     */
    public SimpleExceptionHandler(final LoggerBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleException(final Exception e, final String message) {
        this.broadcaster.broadcastError(message + " --" + ExceptionUtils.getStackTrace(e));
    }

}
