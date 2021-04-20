/**
 *
 */
package reega.viewutils;

import java.util.function.Consumer;

/**
 * @param <T> type of the viewmodel
 */
@FunctionalInterface
public interface ViewModelChangedEventHandler<T extends ViewModel> extends EventHandler<Class<? extends T>> {
    /**
     * {@inheritDoc} Calls {@link #handle(EventArgs, Consumer, boolean, ViewModelChangedEventType)} with no action to
     * execute after creation, no clearance of the navigation stack and the PUSH event type
     */
    @Override
    default void handle(final EventArgs<Class<? extends T>> eventArgs) {
        this.handle(eventArgs, null, false, ViewModelChangedEventType.PUSH);
    }

    /**
     * Handle a viewmodel change event that can have an action that needs to be invoked after the creation of the
     * controller.
     *
     * @param eventArgs                    event args
     * @param actionToExecuteAfterCreation action to execute after the creation of the viewmodel
     * @param clearNavigationStack         boolean to indicate if the clearance of the navigation stack is needed
     * @param eventType                    {@link ViewModelChangedEventType} needed for this operation
     * @throws IllegalArgumentException if <code>eventType</code> is {@link ViewModelChangedEventType#POP} and
     *                                  <code>actionToExecuteAfterCreation</code> is not null
     *
     * @see ViewModelChangedArgs
     */
    default void handle(final EventArgs<Class<? extends T>> eventArgs, final Consumer<T> actionToExecuteAfterCreation,
            final boolean clearNavigationStack, final ViewModelChangedEventType eventType) {
        this.handle(new ViewModelChangedArgs<>(eventArgs.getEventItem(), eventArgs.getSource(),
                actionToExecuteAfterCreation, clearNavigationStack, eventType));
    }

    /**
     * Handle a viewmodel change event with its {@link ViewModelChangedArgs}.
     *
     * @param args event args to use
     */
    void handle(ViewModelChangedArgs<T> args);

    /**
     * Event args for the {@link ViewModelChangedEventHandler}.
     *
     * @param <T> type of the viewmodel of the {@link ViewModelChangedEventHandler}
     */
    final class ViewModelChangedArgs<T extends ViewModel> extends EventArgs<Class<? extends T>> {

        private final Consumer<T> actionToExecuteAfterCreation;
        private final boolean clearNavigationStack;
        private final ViewModelChangedEventType eventType;

        /**
         *
         * @param eventItem                    viewmodel class to push
         * @param source                       source object
         * @param actionToExecuteAfterCreation action to execute after the creation of the object if the
         *                                     <code>eventType</code> is {@link ViewModelChangedEventType#PUSH}
         * @param clearNavigationStack         boolean to indicate if the clearance of the navigation stack is needed
         * @param eventType                    {@link ViewModelChangedEventType} needed for this operation
         * @throws IllegalArgumentException if <code>eventType</code> is {@link ViewModelChangedEventType#POP} and
         *                                  <code>actionToExecuteAfterCreation</code> is not null
         */
        public ViewModelChangedArgs(final Class<? extends T> eventItem, final Object source,
                final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack,
                final ViewModelChangedEventType eventType) {
            super(eventItem, source);
            if (eventType == ViewModelChangedEventType.POP && actionToExecuteAfterCreation != null) {
                throw new IllegalArgumentException(
                        "You cannot execute an action if the controller changed event type is a POP request");
            }
            this.actionToExecuteAfterCreation = actionToExecuteAfterCreation;
            this.clearNavigationStack = clearNavigationStack;
            this.eventType = eventType;
        }

        /**
         * Get the action to execute after the creation of the viewmodel.
         *
         * @return the action to execute after the creation of the viewmodel
         */
        public Consumer<T> getActionToExecuteAfterCreation() {
            return this.actionToExecuteAfterCreation;
        }

        /**
         * Execute {@link #getActionToExecuteAfterCreation()} only if it's not null.
         *
         * @param viewmodel viewmodel used for the argument of the action
         */
        public void executeAction(final T viewmodel) {
            if (this.actionToExecuteAfterCreation != null) {
                this.actionToExecuteAfterCreation.accept(viewmodel);
            }
        }

        /**
         * Boolean to indicate if there's a need to clear the navigation stack before pushing the new viewmodel.
         *
         * @return true if you want to clear the navigation stack, false otherwise
         */
        public boolean isClearNavigationStack() {
            return this.clearNavigationStack;
        }

        /**
         * Get the {@link ViewModelChangedEventType} of the request.
         *
         * @return a {@link ViewModelChangedEventType} that is the event type of the request
         */
        public ViewModelChangedEventType getEventType() {
            return this.eventType;
        }

    }

    enum ViewModelChangedEventType {
        PUSH, POP
    }
}
