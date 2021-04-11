package reega.views;

import javafx.beans.property.ObjectProperty;
import reega.controllers.OperatorMainController;
import reega.users.User;

public class OperatorMainView extends MainView {

    public OperatorMainView(final OperatorMainController controller) {
        super(controller);
        // If the user is not null then populate the services pane with the current data
        if (controller.user().isNotNull().get()) {
            // If a user is already selected then populate the contracts pane with all the contracts of the user
            ObjectProperty<User> selectedUserProperty = controller.selectedUser();
            if (selectedUserProperty.isNotNull().get()) {
                this.getManagedUser().visibleProperty().set(true);
                this.getManagedUser().setText("Managing user " + selectedUserProperty.get().getFullName());
                this.populateContractsPane(controller);
            }
            // Then populate the data in the servicesPane
            this.populateServicesPane(controller);

        }
        // When the selected user changes than populate the contracts and servicesPane
        controller.selectedUser().addListener((observable, oldValue, newValue) -> {
            this.populateServicesPane(controller);
            this.populateContractsPane(controller);
            this.getManagedUser().visibleProperty().set(false);
            if (newValue != null) {
                this.getManagedUser().visibleProperty().set(true);
                this.getManagedUser().setText("Managing user " + newValue.getFullName());
            }
        });

        // When the user changes populate only the services pane
        controller.user().addListener((observable, oldValue, newValue) -> {
            this.populateServicesPane(controller);
        });

        this.getManagedUser().managedProperty().bind(this.getManagedUser().visibleProperty());
    }

}
