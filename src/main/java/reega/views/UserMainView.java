package reega.views;

import reega.controllers.MainController;

public class UserMainView extends MainView {

    public UserMainView(final MainController controller) {
        super(controller);
        this.populateServicesPane(controller);
        this.populateContractsPane(controller);
    }
}
