package reega.controllers;

import javax.inject.Inject;

import reega.viewutils.Controller;
import reega.viewutils.ControllerChangedEventHandler.ControllerChangedEventType;
import reega.viewutils.Navigator;

public class MasterController {
    private final Navigator navigator;
    private Controller selectedController;

    @Inject
    public MasterController(final Navigator navigator) {
        this.navigator = navigator;
        this.navigator.selectedControllerProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedController = newValue;
            this.selectedController.setControllerChangeEvent(evtArgs -> {
                if (evtArgs.getEventType() == ControllerChangedEventType.POP) {
                    this.navigator.popController();
                    return;
                }
                final Controller controller = this.navigator.buildController(evtArgs.getEventItem());
                evtArgs.executeAction(controller);
                this.navigator.pushControllerToStack(controller, evtArgs.isClearNavigationStack());
            });
        });
    }

    /**
     * Start the app with the default controller
     */
    public void initializeApp() {
        this.navigator.pushController(LoginController.class, false);
    }

    /**
     * Pop the current controller
     *
     * @see Navigator
     */
    public void popController() {
        this.navigator.popController();
    }

    /**
     * Get the main navigator of the app
     *
     * @return the main navigator of the app
     */
    public Navigator getNavigator() {
        return this.navigator;
    }
}
