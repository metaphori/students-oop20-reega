package reega.views;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import reega.controllers.MainController;
import reega.controllers.OperatorMainController;
import reega.data.models.Contract;
import reega.viewcomponents.Card;

import java.util.stream.Collectors;

public class OperatorMainView extends MainView{

    public OperatorMainView(OperatorMainController controller) {
        super(controller);
        //If the user is not null then populate the services pane with the current data
        if (controller.user().isNotNull().get()) {
            //If a user is already selected then populate the contracts pane with all the contracts of the user
            if (controller.selectedUser().isNotNull().get()) {
                this.populateContractsPane(controller);
            }
            //Then populate the data in the servicesPane
            this.populateServicesPane(controller);


        }
        //When the selected user changes than populate the contracts and servicesPane
        controller.selectedUser().addListener((observable, oldValue, newValue) -> {
            this.populateServicesPane(controller);
            this.populateContractsPane(controller);
        });

        //When the user changes populate only the services pane
        controller.user().addListener((observable,oldValue,newValue) -> {
            this.populateServicesPane(controller);
        });
    }



}
