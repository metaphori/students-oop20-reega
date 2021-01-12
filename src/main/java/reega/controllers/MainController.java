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
            this.selectedController
                    .setControllerChangeEvent(event -> this.navigator.pushController(event.getEventItem()));
        });
    }

    public void initializeApp() {
        this.navigator.pushController(LoginController.class);
    }

    public Navigator getNavigator() {
        return this.navigator;
    }
}
