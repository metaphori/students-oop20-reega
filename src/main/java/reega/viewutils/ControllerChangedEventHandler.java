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
     * {@inheritDoc} Calls {@link #handle(EventArgs, Consumer, boolean, ControllerChangedEventType)} with no action to
     * execute after creation, no clearance of the navigation stack and the PUSH event type
     */
    @Override
    default void handle(final EventArgs<Class<? extends T>> eventArgs) {
        this.handle(eventArgs, null, false, ControllerChangedEventType.PUSH);
    }

    /**
     * Handle a controller change event that can have an action that needs to be invoked after the creation of the
     * controller
     *
     * @param eventArgs                    event args
     * @param actionToExecuteAfterCreation action to execute after the creation of the controller
     * @param clearNavigationStack         boolean to indicate if the clearance of the navigation stack is needed
     * @param eventType                    {@link ControllerChangedEventType} needed for this operation
     * @throws IllegalArgumentException if <code>eventType</code> is {@link ControllerChangedEventType#POP} and
     *                                  <code>actionToExecuteAfterCreation</code> is not null
     *
     * @see ControllerChangedEventHandlerArgs
     */
    default void handle(final EventArgs<Class<? extends T>> eventArgs, final Consumer<T> actionToExecuteAfterCreation,
            final boolean clearNavigationStack, final ControllerChangedEventType eventType) {
        this.handle(new ControllerChangedEventHandlerArgs<>(eventArgs.getEventItem(), eventArgs.getSource(),
                actionToExecuteAfterCreation, clearNavigationStack, eventType));
    }

    /**
     * Handle a controller change event with its {@link ControllerChangedEventHandlerArgs}
     *
     * @param args event args to use
     */
    void handle(ControllerChangedEventHandlerArgs<T> args);

    /**
     * Event args for the {@link ControllerChangedEventHandler}
     *
     * @author Marco
     *
     * @param <T> type of the controller of the {@link ControllerChangedEventHandler}
     */
    public static final class ControllerChangedEventHandlerArgs<T extends Controller>
            extends EventArgs<Class<? extends T>> {

        private final Consumer<T> actionToExecuteAfterCreation;
        private final boolean clearNavigationStack;
        private final ControllerChangedEventType eventType;

        /**
         *
         * @param eventItem                    controller class to push
         * @param source                       source object
         * @param actionToExecuteAfterCreation action to execute after the creation of the object if the
         *                                     <code>eventType</code> is {@link ControllerChangedEventType#PUSH}
         * @param clearNavigationStack         boolean to indicate if the clearance of the navigation stack is needed
         * @param eventType                    {@link ControllerChangedEventType} needed for this operation
         * @throws IllegalArgumentException if <code>eventType</code> is {@link ControllerChangedEventType#POP} and
         *                                  <code>actionToExecuteAfterCreation</code> is not null
         */
        public ControllerChangedEventHandlerArgs(final Class<? extends T> eventItem, final Object source,
                final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack,
                final ControllerChangedEventType eventType) {
            super(eventItem, source);
            if (eventType == ControllerChangedEventType.POP && actionToExecuteAfterCreation != null) {
                throw new IllegalArgumentException(
                        "You cannot execute an action if the controller changed event type is a POP request");
            }
            this.actionToExecuteAfterCreation = actionToExecuteAfterCreation;
            this.clearNavigationStack = clearNavigationStack;
            this.eventType = eventType;
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
         * Boolean to indicate if there's a need to clear the navigation stack before pushing the new controller
         *
         * @return true if you want to clear the navigation stack, false otherwise
         */
        public boolean isClearNavigationStack() {
            return this.clearNavigationStack;
        }

        /**
         * Get the {@link ControllerChangedEventType} of the request
         *
         * @return a {@link ControllerChangedEventType} that is the event type of the request
         */
        public ControllerChangedEventType getEventType() {
            return this.eventType;
        }

    }

    public enum ControllerChangedEventType {
        PUSH, POP
    }
}
