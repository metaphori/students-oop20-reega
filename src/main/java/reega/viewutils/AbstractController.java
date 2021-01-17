package reega.viewutils;

import java.util.function.Consumer;

/**
 * Controller that every controller needs to inherit
 *
 * @author Marco
 *
 */
public abstract class AbstractController implements Controller {
    private ControllerChangedEventHandler<Controller> controllerChangeEvent;

    /**
     * Push a new controller
     *
     * @param <T>             type of the new controller
     * @param controllerClass class of the new controller
     */
    protected <T extends Controller> void pushController(final Class<T> controllerClass) {
        this.pushController(controllerClass, null);
    }

    /**
     * Push a new controller and execute {@code actionToExecuteAfterCreation} after
     * it has been created
     *
     * @param <T>             type of the new controller
     * @param controllerClass class of the new controller
     * @param actionToExecute action to execute after its creation
     */
    @SuppressWarnings("unchecked")
    protected <T extends Controller> void pushController(final Class<T> controllerClass,
            final Consumer<T> actionToExecuteAfterCreation) {
        if (this.controllerChangeEvent == null) {
            return;
        }
        this.controllerChangeEvent.handle(new EventArgs<Class<Controller>>((Class<Controller>) controllerClass, this),
                (Consumer<Controller>) actionToExecuteAfterCreation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setControllerChangeEvent(final ControllerChangedEventHandler<Controller> controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }
}
