/**
 *
 */
package reega.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marco
 *
 */
public class SimpleExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleExceptionHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleException(final Exception e, final String message) {
        SimpleExceptionHandler.LOGGER.error(message + " -- " + ExceptionUtils.getStackTrace(e));
    }

}
