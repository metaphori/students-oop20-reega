package reega.viewutils;

/**
 * Handler of events
 *
 * @author Marco
 *
 * @param <T> type for the EventArgs
 */
@FunctionalInterface
public interface EventHandler<T> {
    /**
     * Handle an event
     *
     * @param eventArgs Event args
     */
    void handle(EventArgs<T> eventArgs);
}
