package reega.logging;

/**
 *
 */
public interface ExceptionHandler {

    /**
     * Try to handle an exception.
     *
     * @param e exception to handle
     */
    default void handleException(final Exception e) {
        this.handleException(e, "");
    }

    /**
     * Try to handle an exception and use {@code message} for other information.
     *
     * @param e       exception to handle
     * @param message message to use for other informations
     */
    void handleException(Exception e, String message);
}
