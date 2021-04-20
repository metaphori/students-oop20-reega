package reega.views;

import javafx.beans.property.ObjectProperty;
import reega.controllers.OperatorMainViewModel;
import reega.users.User;

public class OperatorMainView extends MainView {

    public OperatorMainView(final OperatorMainViewModel viewModel) {
        super(viewModel);

        // If a user is already selected then populate the contracts pane with all the contracts of the user
        final ObjectProperty<User> selectedUserProperty = viewModel.selectedUser();
        if (selectedUserProperty.isNotNull().get()) {
            this.getManagedUser().visibleProperty().set(true);
            this.getManagedUser().setText("Managing user " + selectedUserProperty.get().getFullName());
            this.populateContractsPane(viewModel);
        }
        // Then populate the data in the servicesPane
        this.populateServicesPane(viewModel);

        // When the selected user changes than populate the contracts and servicesPane
        viewModel.selectedUser().addListener((observable, oldValue, newValue) -> {
            this.populateServicesPane(viewModel);
            this.populateContractsPane(viewModel);
            if (newValue != null) {
                this.getManagedUser().setText("Managing user " + newValue.getFullName());
            }
            this.getManagedUser().visibleProperty().set(newValue != null);
        });

        this.getManagedUser().managedProperty().bind(this.getManagedUser().visibleProperty());
    }

}
