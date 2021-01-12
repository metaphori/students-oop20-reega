package reega.viewutils;

/**
 * Controller that every controller needs to inherit
 *
 * @author Marco
 *
 */
public abstract class AbstractController implements Controller {
    private EventHandler<Class<? extends Controller>> controllerChangeEvent;

    /**
     * Push a new controller
     *
     * @param <T>             type of the new controller
     * @param controllerClass class of the new controller
     */
    protected <T extends Controller> void pushController(final Class<T> controllerClass) {
        if (this.controllerChangeEvent == null) {
            return;
        }
        this.controllerChangeEvent.handle(new EventArgs<>(controllerClass, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setControllerChangeEvent(final EventHandler<Class<? extends Controller>> controllerChangeEvent) {
        this.controllerChangeEvent = controllerChangeEvent;
    }
}
