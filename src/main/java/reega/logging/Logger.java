/**
 *
 */
package reega.logging;

/**
 * @author Marco
 *
 */
public interface Logger {
    /**
     * Log {@code message}
     *
     * @param message message to log
     */
    void log(String message);

    /**
     * Log {@code message} as an error
     *
     * @param message error to log
     */
    void logError(String message);
}
