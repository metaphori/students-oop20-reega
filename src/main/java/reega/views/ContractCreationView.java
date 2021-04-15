package reega.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import reega.controllers.ContractCreationController;
import reega.data.models.gson.NewContract;
import reega.viewutils.DialogFactory;

import java.io.IOException;

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

                this.userLabel.setText("User: " + controller.getUser().get().getName() + controller.getUser().get().getSurname());
                // TODO init services list properly
                this.contractButton.setOnAction(e -> {
                        try {
                                controller.registerContract(getContractFromView());
                        } catch (IllegalArgumentException illegalArgumentException) {
                                DialogFactory.buildAlert(Alert.AlertType.ERROR, "couldn't create contract", illegalArgumentException.getMessage())
                                        .showAndWait();
                        }
                });
        }

        protected NewContract getContractFromView() {
                // TODO fetch info and pass new contract
                return null;
        }
}
