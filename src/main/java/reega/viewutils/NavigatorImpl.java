package reega.viewutils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Stack;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.util.ServiceProvider;

public class NavigatorImpl implements Navigator {

    private final Stack<ViewModel> navigationStack = new Stack<>();
    private final ServiceProvider serviceProvider;
    private final ObjectProperty<ViewModel> selectedViewModelProperty = new SimpleObjectProperty<>();
    private final BooleanProperty navigationStackNotEmptyProperty = new SimpleBooleanProperty(false);

    public NavigatorImpl(final ServiceProvider provider) {
        this.serviceProvider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ViewModel> T buildViewModel(final Class<T> viewModelClass) {
        final Optional<T> optionalViewModel = this.serviceProvider.getService(viewModelClass);
        if (optionalViewModel.isEmpty()) {
            throw new NoSuchElementException();
        }
        return optionalViewModel.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushViewModelToStack(final ViewModel viewModel, final boolean clearNavigationStack) {
        if (clearNavigationStack) {
            this.navigationStack.clear();
        }
        this.navigationStack.push(viewModel);
        this.navigationStackNotEmptyProperty.set(this.navigationStack.size() > 1);
        this.selectedViewModelProperty.set(viewModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void popController() {
        if (this.navigationStackNotEmptyProperty.getValue().equals(true)) {
            this.navigationStack.pop();
            this.navigationStackNotEmptyProperty.set(this.navigationStack.size() > 1);
            this.selectedViewModelProperty.set(this.navigationStack.peek());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectProperty<ViewModel> selectedViewModelProperty() {
        return this.selectedViewModelProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BooleanProperty navigationStackNotEmptyProperty() {
        return this.navigationStackNotEmptyProperty;
    }
}
