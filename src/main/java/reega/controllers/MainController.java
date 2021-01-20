package reega.controllers;

import javax.inject.Inject;

import reega.viewutils.Controller;
import reega.viewutils.Navigator;

public class MainController {
    private final Navigator navigator;
    private Controller selectedController;

    @Inject
    public MainController(final Navigator navigator) {
        this.navigator = navigator;
        this.navigator.selectedControllerProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedController = newValue;
            this.selectedController.setControllerChangeEvent(evtArgs -> {
                final Controller controller = this.navigator.pushController(evtArgs.getEventItem(),
                        evtArgs.isClearNavigationStack());
                evtArgs.executeAction(controller);
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
