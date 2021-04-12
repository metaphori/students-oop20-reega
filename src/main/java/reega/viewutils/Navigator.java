package reega.viewutils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

public interface Navigator {
    /**
     * Build a controller
     * @param controllerClass class of the controller
     * @param <T> type of the controller
     * @return an instance of the controller
     */
    <T extends Controller> T buildController(Class<T> controllerClass);

    /**
     * Push <code>controller</code> to the navigation stack
     * @param controller controller that needs to be pushed
     * @param clearNavigationStack clear the navigation stack before pushing the controller
     */
    void pushControllerToStack(Controller controller,boolean clearNavigationStack);
    /**
     * Build a controller and push it into the navigation stack
     *
     * @param <T>                  type of the controller
     * @param controllerClass      class of the controller
     * @param clearNavigationStack clear the navigation stack before pushing the controller
     */
    default <T extends Controller> void pushController(Class<T> controllerClass, boolean clearNavigationStack) {
        T controller = this.buildController(controllerClass);
        this.pushControllerToStack(controller, clearNavigationStack);
    }

    /**
     * Pop the current controller that is in the peek of the stack {@link #selectedControllerProperty()}
     */
    void popController();

    /**
     * Property representing the current controller in the scene
     *
     * @return a {@link ObjectProperty} representing the current controller in the scene
     */
    ObjectProperty<Controller> selectedControllerProperty();

    /**
     * Boolean property representing the state of the navigation stack
     *
     * @return a {@link BooleanProperty} representing the current state of the navigation stack
     */
    BooleanProperty navigationStackNotEmptyProperty();
}
