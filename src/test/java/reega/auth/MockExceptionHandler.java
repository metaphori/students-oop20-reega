/**
 *
 */
package reega.auth;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.logging.ExceptionHandler;

/**
 * @author Marco
 *
 */
public class MockExceptionHandler implements ExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(MockExceptionHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleException(final Exception e, final String message) {
        this.logger.error(message + ExceptionUtils.getStackTrace(e));
    }

}
