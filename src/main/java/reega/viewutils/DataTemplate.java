/**
 *
 */
package reega.viewutils;

import java.util.function.Supplier;

/**
 * @param <T> type of the data object of this {@link DataTemplate}
 */
public interface DataTemplate<T> {
    /**
     * Get the class of the data object.
     *
     * @return the class of the data object
     */
    Class<T> getDataObjectClass();

    /**
     * Get the factory for creating a control based on the controller.
     *
     * @param controller controller to use
     * @return a {@link ReegaView} that is the view representation of the controller
     */
    Supplier<? extends ReegaView> getControlFactory(T controller);
}
