package reega.viewutils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Stack;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.util.ServiceProvider;

public class NavigatorImpl implements Navigator {

    private final Stack<Object> navigationStack = new Stack<>();
    private final ServiceProvider serviceProvider;
    private final ObjectProperty<Controller> selectedControllerProperty = new SimpleObjectProperty<>();

    public NavigatorImpl(final ServiceProvider provider) {
        this.serviceProvider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Controller> T pushController(final Class<T> controllerClass) {
        final Optional<T> optionalController = this.serviceProvider.getService(controllerClass);
        if (optionalController.isEmpty()) {
            throw new NoSuchElementException();
        }
        final T controller = optionalController.get();
        this.navigationStack.push(controller);
        this.selectedControllerProperty.set(controller);
        return controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectProperty<Controller> selectedControllerProperty() {
        return this.selectedControllerProperty;
    }
}
