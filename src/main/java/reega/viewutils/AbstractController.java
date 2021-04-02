package reega.viewutils;

import java.util.function.Consumer;

import reega.viewutils.ControllerChangedEventHandler.ControllerChangedEventType;

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
     * @param <T>                  type of the new controller
     * @param controllerClass      class of the new controller
     * @param clearNavigationStack clear the navigation stack before pushing the new controller
     */
    protected <T extends Controller> void pushController(final Class<T> controllerClass,
            final boolean clearNavigationStack) {
        this.pushController(controllerClass, null, clearNavigationStack);
    }

    /**
     * Push a new controller and execute {@code actionToExecuteAfterCreation} after it has been created
     *
     * @param <T>                          type of the new controller
     * @param controllerClass              class of the new controller
     * @param actionToExecuteAfterCreation action to execute after its creation
     * @param clearNavigationStack         clear the navigation stack before pushing the new controller
     */
    protected final <T extends Controller> void pushController(final Class<T> controllerClass,
            final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack) {
        this.pushOrPopController(controllerClass, actionToExecuteAfterCreation, clearNavigationStack,
                ControllerChangedEventType.PUSH);
    }

    /**
     * Pop the peek controller in the stack
     */
    protected final void popController() {
        this.pushOrPopController(null, null, false, ControllerChangedEventType.POP);
    }

    /**
     * Push or pop a new controller
     *
     * @param <T>                          type of the new controller
     * @param controllerClass              class of the new controller
     * @param actionToExecuteAfterCreation action to execute after its creation (only if it's a
     *                                     {@link ControllerChangedEventType#PUSH}
     * @param clearNavigationStack         clear the navigation stack before pushing the new controller
     */
    @SuppressWarnings("unchecked")
    private final <T extends Controller> void pushOrPopController(final Class<T> controllerClass,
            final Consumer<T> actionToExecuteAfterCreation, final boolean clearNavigationStack,
            final ControllerChangedEventType eventType) {
        if (this.controllerChangeEvent == null) {
            return;
        }
        this.controllerChangeEvent.handle(new EventArgs<>(controllerClass, this),
                (Consumer<Controller>) actionToExecuteAfterCreation, clearNavigationStack, eventType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setControllerChangeEvent(final ControllerChangedEventHandler<Controller> controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }
}
