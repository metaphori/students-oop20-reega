package reega.viewutils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

public interface Navigator {
    /**
     * Build a {@link ViewModel}
     * @param controllerClass class of the viewModel
     * @param <T> type of the viewModel
     * @return an instance of the viewModel
     */
    <T extends ViewModel> T buildViewModel(Class<T> controllerClass);

    /**
     * Push <code>viewModel</code> to the navigation stack
     * @param viewModel viewModel that needs to be pushed
     * @param clearNavigationStack clear the navigation stack before pushing the viewModel
     */
    void pushViewModelToStack(ViewModel viewModel, boolean clearNavigationStack);
    /**
     * Build a viewModel and push it into the navigation stack
     *
     * @param <T>                  type of the viewModel
     * @param viewModelClass      class of the viewModel
     * @param clearNavigationStack clear the navigation stack before pushing the viewModel
     */
    default <T extends ViewModel> void pushViewModel(Class<T> viewModelClass, boolean clearNavigationStack) {
        T controller = this.buildViewModel(viewModelClass);
        this.pushViewModelToStack(controller, clearNavigationStack);
    }

    /**
     * Pop the current viewModel that is in the peek of the stack {@link #selectedViewModelProperty()}
     */
    void popController();

    /**
     * Property representing the current viewModel in the scene
     *
     * @return a {@link ObjectProperty} representing the current viewModel
     */
    ObjectProperty<ViewModel> selectedViewModelProperty();

    /**
     * Boolean property representing the state of the navigation stack
     *
     * @return a {@link BooleanProperty} representing the current state of the navigation stack
     */
    BooleanProperty navigationStackNotEmptyProperty();
}
