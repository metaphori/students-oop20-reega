package reega.views;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import reega.controllers.ContractCreationController;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.users.User;
import reega.viewutils.DialogFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ContractCreationView extends VBox {

        @FXML
        private Label userLabel;
        @FXML
        private ListView servicesList;
        @FXML
        private TextField addressLabel;
        @FXML
        private Button contractButton;

        public ContractCreationView(ContractCreationController controller) {
                final FXMLLoader loader = new FXMLLoader(
                        ClassLoader.getSystemClassLoader().getResource("views/ContractCreation.fxml"));

                loader.setRoot(this);
                loader.setController(this);

                try {
                        loader.load();
                } catch (final IOException e) {
                        e.printStackTrace();
                }
                this.userLabel.setText("User: " + controller.getUser().getName() + controller.getUser().getSurname());
                // list view init
                this.servicesList.setItems(FXCollections.observableList(List.of(ServiceType.values())));
                this.servicesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                // TODO crea una checkboxlist con un convertitore adatto da svctype a string
                /*new CheckBoxListCell<ServiceType>() {
                        {this.setConverter();}
                }
                this.servicesList.setCellFactory(new Callback<ListView<ServiceType>, ListCell<ServiceType>>() {

                        @Override
                        public ListCell<ServiceType> call(ListView<ServiceType> param) {
                                CheckBoxListCell<ServiceType> cell = new CheckBoxListCell<>();
                                cell.setConverter();
                                return cell;
                });*/
                // button init
                this.contractButton.setOnAction(e -> {
                        try {
                                if(controller.registerContract(getContractFromView(controller.getUser()))) {
                                        DialogFactory.buildAlert(Alert.AlertType.INFORMATION, "contract created succsefully", "Contract has been created succesfully", new ButtonType("ok")).showAndWait();
                                }
                        } catch (IllegalArgumentException illegalArgumentException) {
                                DialogFactory.buildAlert(Alert.AlertType.ERROR, "couldn't create contract", illegalArgumentException.getMessage())
                                        .showAndWait();
                        }
                });
        }

        protected NewContract getContractFromView(User user) {
                // TODO fetch info and pass new contract in a decent manner (also ceck fiscal code)
                return new NewContract(this.addressLabel.getText(),
                        (List<ServiceType>) this.servicesList.getSelectionModel().getSelectedItems().stream().collect(
                                Collectors.toList()), user.getFiscalCode(), new Date());
                /*
                * DialogFactory.buildAlert(Alert.AlertType.WARNING, "invalid contract", "the information selected for the contract are not valid").showAndWait();
                */
        }
}
