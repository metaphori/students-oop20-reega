package reega.viewutils;

import javafx.beans.property.ObjectProperty;

public interface Navigator {
    /**
     * Push a controller in the navigation stack
     *
     * @param <T>             type of the controller
     * @param controllerClass class of the controller
     * @return an instance of the controller
     */
    <T extends Controller> T pushController(Class<T> controllerClass);

    /**
     * Property representing the current controller in the scene
     *
     * @return a {@link ObjectProperty} representing the current controller in the
     *         scene
     */
    ObjectProperty<Controller> selectedControllerProperty();
}
