package reega.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import reega.controllers.ContractCreationViewModel;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.users.User;
import reega.viewutils.DialogFactory;
import reega.viewutils.ReegaFXMLLoader;
import reega.viewutils.ViewUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContractCreationView extends VBox {

        @FXML private Label userLabel;
        @FXML private HBox servicesBox;
        @FXML private TextField addressField;
        @FXML private Button contractButton;

        public ContractCreationView(ContractCreationViewModel viewModel) {
                ReegaFXMLLoader.loadFXML(this,"views/ContractCreation.fxml");
                this.userLabel.setText("User: " + viewModel.getUser().getName() + viewModel.getUser().getSurname());
                // services list init
                this.servicesBox.getChildren().addAll(List.of(ServiceType.values()).stream().map(svc -> {
                        CheckBox box = ViewUtils.wrapNodeWithStyleClasses(
                                new CheckBox(StringUtils.capitalize(svc.getName())), "svcBox");
                        box.setUserData(svc);
                        return box;
                }).collect(Collectors.toList()));
                // button init
                this.contractButton.setOnAction(e -> {
                        Optional<NewContract> newContract = getContractFromView(viewModel.getUser());
                        if(newContract.isPresent()) {
                                if (viewModel.registerContract(newContract.get())) {
                                        DialogFactory.buildAlert(Alert.AlertType.INFORMATION,
                                                "contract created successfully", "Contract has been created " + "successfully", ButtonType.OK)
                                                .showAndWait();
                                } else {
                                        DialogFactory.buildAlert(Alert.AlertType.ERROR, "couldn't create contract",
                                                "There was an error trying to register the contract").showAndWait();
                                }
                        }

                });
        }

        protected Optional<NewContract> getContractFromView(User user) {
                if(this.addressField.getText().isBlank()) {
                        DialogFactory.buildAlert(Alert.AlertType.WARNING, "Address not selected",
                                "Insert at least one address").showAndWait();
                        return Optional.empty();
                }
                List<ServiceType> selectedSvcType = this.servicesBox.getChildren()
                        .stream()
                        .filter(cb -> ((CheckBox) cb).isSelected())
                        .map(cb -> (ServiceType) cb.getUserData())
                        .collect(Collectors.toList());
                if (selectedSvcType.isEmpty()) {
                        DialogFactory.buildAlert(Alert.AlertType.WARNING, "Services not selected",
                                "Select at least one ServiceType").showAndWait();
                        return Optional.empty();
                }
                return Optional.of(new NewContract(StringUtils.capitalize(this.addressField.getText()), selectedSvcType,
                        user.getFiscalCode(), new Date()));
        }
}
