/**
 *
 */
package reega.viewutils;

import java.util.function.Consumer;

/**
 * @author Marco
 *
 */
@FunctionalInterface
public interface ControllerChangedEventHandler<T extends Controller> extends EventHandler<Class<T>> {
    /**
     * {@inheritDoc}
     */
    @Override
    default void handle(final EventArgs<Class<T>> eventArgs) {
        this.handle(eventArgs, null, false);
    }

    /**
     * Handle a controller change event that can have an action that needs to be
     * invoked after the creation of the controller
     *
     * @param eventArgs                    event args
     * @param actionToExecuteAfterCreation action to execute after the creation of
     *                                     the controller
     */
    void handle(final EventArgs<Class<T>> eventArgs, final Consumer<T> actionToExecuteAfterCreation,
            boolean clearNavigationStack);
}
