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
public interface ControllerChangedEventHandler<T extends Controller> extends EventHandler<Class<? extends T>> {
    /**
     * {@inheritDoc}
     */
    @Override
    default void handle(final EventArgs<Class<? extends T>> eventArgs) {
        this.handle(eventArgs, null, false);
    }

    /**
     * Handle a controller change event that can have an action that needs to be
     * invoked after the creation of the controller
     *
     * @param eventArgs                    event args
     * @param actionToExecuteAfterCreation action to execute after the creation of
     *                                     the controller
     *
     * @see ControllerChangedEventHandlerArgs
     */
    default void handle(final EventArgs<Class<? extends T>> eventArgs, final Consumer<T> actionToExecuteAfterCreation,
            final boolean clearNavigationStack) {
        this.handle(new ControllerChangedEventHandlerArgs<>(eventArgs.getEventItem(), eventArgs.getSource(),
                actionToExecuteAfterCreation, clearNavigationStack));
    }

    /**
     * Handle a controller change event with its
     * {@link ControllerChangedEventHandlerArgs}
     *
     * @param args event args to use
     */
    void handle(ControllerChangedEventHandlerArgs<T> args);

    /**
     * Event args for the {@link ControllerChangedEventHandler}
     *
     * @author Marco
     *
     * @param <T> type of the controller of the
     *            {@link ControllerChangedEventHandler}
     */
    public static final class ControllerChangedEventHandlerArgs<T extends Controller>
            extends EventArgs<Class<? extends T>> {

        private final Consumer<T> actionToExecuteAfterCreation;
        private final boolean clearNavigationStack;

        public ControllerChangedEventHandlerArgs(final Class<? extends T> eventItem, final Object source,
                final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack) {
            super(eventItem, source);
            this.actionToExecuteAfterCreation = actionToExecuteAfterCreation;
            this.clearNavigationStack = clearNavigationStack;
        }

        /**
         * Get the action to execute after the creation of the controller
         *
         * @return the action to execute after the creation of the controller
         */
        public Consumer<T> getActionToExecuteAfterCreation() {
            return this.actionToExecuteAfterCreation;
        }

        /**
         * Execute {@link #getActionToExecuteAfterCreation()} only if it's not null
         *
         * @param controller controller used for the argument of the action
         */
        public void executeAction(final T controller) {
            if (this.actionToExecuteAfterCreation != null) {
                this.actionToExecuteAfterCreation.accept(controller);
            }
        }

        /**
         * Boolean to indicate if there's a need to clear the navigation stack before
         * pushing the new controller
         *
         * @return true if you want to clear the navigation stack, false otherwise
         */
        public boolean isClearNavigationStack() {
            return this.clearNavigationStack;
        }

    }
}
