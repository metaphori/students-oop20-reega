package reega.viewutils;

import java.util.function.Consumer;

import reega.viewutils.ViewModelChangedEventHandler.ViewModelChangedEventType;

/**
 * Controller that every controller needs to inherit
 *
 * @author Marco
 *
 */
public abstract class AbstractViewModel implements ViewModel {
    private ViewModelChangedEventHandler<ViewModel> viewModelChangedEventHandler;

    /**
     * Push a new viewModel
     *
     * @param <T>                  type of the new viewModel
     * @param viewModelClass      class of the new viewModel
     * @param clearNavigationStack clear the navigation stack before pushing the new viewModel
     */
    protected <T extends ViewModel> void pushViewModel(final Class<T> viewModelClass,
                                                       final boolean clearNavigationStack) {
        this.pushViewModel(viewModelClass, null, clearNavigationStack);
    }

    /**
     * Push a new viewModel and execute {@code actionToExecuteAfterCreation} after it has been created
     *
     * @param <T>                          type of the new viewModel
     * @param viewModelClass              class of the new viewModel
     * @param actionToExecuteAfterCreation action to execute after its creation
     * @param clearNavigationStack         clear the navigation stack before pushing the new viewModel
     */
    protected final <T extends ViewModel> void pushViewModel(final Class<T> viewModelClass,
                                                             final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack) {
        this.pushOrPopViewModel(viewModelClass, actionToExecuteAfterCreation, clearNavigationStack,
                ViewModelChangedEventType.PUSH);
    }

    /**
     * Pop the peek controller in the stack
     */
    protected final void popViewModel() {
        this.pushOrPopViewModel(null, null, false, ViewModelChangedEventType.POP);
    }

    /**
     * Push or pop a new viewModel
     *
     * @param <T>                          type of the new viewModel
     * @param viewModelClass              class of the new viewModel
     * @param actionToExecuteAfterCreation action to execute after its creation (only if it's a
     *                                     {@link ViewModelChangedEventType#PUSH}
     * @param clearNavigationStack         clear the navigation stack before pushing the new viewModel
     */
    @SuppressWarnings("unchecked")
    private final <T extends ViewModel> void pushOrPopViewModel(final Class<T> viewModelClass,
                                                                final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack,
                                                                final ViewModelChangedEventType eventType) {
        if (this.viewModelChangedEventHandler == null) {
            return;
        }
        this.viewModelChangedEventHandler.handle(new EventArgs<>(viewModelClass, this),
                (Consumer<ViewModel>) actionToExecuteAfterCreation, clearNavigationStack, eventType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setViewModelChangedEventHandler(final ViewModelChangedEventHandler<ViewModel> viewModelChangedEventHandler) {
        this.viewModelChangedEventHandler = viewModelChangedEventHandler;
    }
}
