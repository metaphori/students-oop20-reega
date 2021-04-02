package reega.views;

import reega.controllers.MainController;

public class UserMainView extends MainView {

    public UserMainView(final MainController controller) {
        super(controller);
        if (controller.user().isNotNull().get()) {
            this.populateServicesPane(controller);
            this.populateContractsPane(controller);
        }

        controller.user().addListener((observable, oldValue, newValue) -> {
            this.populateServicesPane(controller);
            this.populateContractsPane(controller);
        });
    }
}
