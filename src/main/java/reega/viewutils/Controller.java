package reega.viewutils;

public interface Controller {
    /**
     * Set an {@link EventHandler} that is called whenever a new controller needs to be pushed from this controller
     *
     * @param controllerChangeEvent {@link ControllerChangedEventHandler}
     */
    void setControllerChangeEvent(ControllerChangedEventHandler<Controller> controllerChangeEvent);
}
