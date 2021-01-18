package reega.viewutils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

public interface Navigator {
    /**
     * Push a controller in the navigation stack
     *
     * @param <T>                  type of the controller
     * @param controllerClass      class of the controller
     * @param clearNavigationStack clear the navigation stack before pushing the
     *                             controller
     * @return an instance of the controller
     */
    <T extends Controller> T pushController(Class<T> controllerClass, boolean clearNavigationStack);

    /**
     * Pop the current controller that is in the peek of the stack
     * {@link #selectedControllerProperty()}
     */
    void popController();

    /**
     * Property representing the current controller in the scene
     *
     * @return a {@link ObjectProperty} representing the current controller in the
     *         scene
     */
    ObjectProperty<Controller> selectedControllerProperty();

    /**
     * Boolean property representing the state of the navigation stack
     *
     * @return a {@link BooleanProperty} representing the current state of the
     *         navigation stack
     */
    BooleanProperty navigationStackNotEmptyProperty();
}
