package reega.views;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import reega.controllers.MainController;
import reega.viewcomponents.Card;

public class UserMainView extends MainView{

    public UserMainView(MainController controller) {
        super(controller);
        if (controller.user().isNotNull().get()) {
            this.populateServicesPane(controller);
            this.populateContractsPane(controller);
        }

        controller.user().addListener((observable,oldValue,newValue) -> {
            this.populateServicesPane(controller);
            this.populateContractsPane(controller);
        });
    }
}
