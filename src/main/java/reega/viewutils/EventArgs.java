package reega.viewutils;

/**
 * Arguments for {@link EventHandler}
 *
 * @author Marco
 *
 * @param <T> type of the item involved in the args
 */
public class EventArgs<T> {
    private final T eventItem;
    private final Object source;

    public EventArgs(final T eventItem, final Object source) {
        this.eventItem = eventItem;
        this.source = source;
    }

    /**
     * Get the item involved in this event
     *
     * @return item involved in the event
     */
    public T getEventItem() {
        return this.eventItem;
    }

    /**
     * Get the source object of the event
     *
     * @return the source object of the event
     */
    public Object getSource() {
        return this.source;
    }
}
