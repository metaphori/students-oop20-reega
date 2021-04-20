package reega.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import reega.controllers.UserProfileViewModel;
import reega.users.User;
import reega.util.ValueResult;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewcomponents.WrappableLabel;
import reega.viewutils.DialogFactory;
import reega.viewutils.ReegaFXMLLoader;
import reega.viewutils.ReegaView;
import reega.viewutils.ViewUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class UserProfileView extends VBox implements ReegaView {

    @FXML
    private WrappableLabel userName;
    @FXML
    private WrappableLabel userSurname;
    @FXML
    private WrappableLabel userRole;
    @FXML
    private WrappableLabel userEmail;
    @FXML
    private WrappableLabel userFiscalCode;
    @FXML
    private FlexibleGridPane userContracts;
    @FXML
    private ToggleButton deleteUserButton;

    public UserProfileView(UserProfileViewModel viewModel) {
        ReegaFXMLLoader.loadFXML(this, "views/UserProfile.fxml");

        this.setUserProperties(viewModel.getUser());
        this.buildUserContractsPane(viewModel);
        this.deleteUserButton.setOnAction(e -> {
            ValueResult<Void> deleteResult = viewModel.deleteCurrentUser();
            if (deleteResult.isInvalid()) {
                DialogFactory.buildAlert(Alert.AlertType.ERROR, "Something went wrong with the deletion", deleteResult.getMessage(), ButtonType.CLOSE);
            }
        });
    }

    private void setUserProperties(User user) {
        this.userName.setText("Name: " + StringUtils.capitalize(user.getName()));
        this.userSurname.setText("Surname: " + StringUtils.capitalize(user.getSurname()));
        this.userRole.setText("Role: " + StringUtils.capitalize(user.getRole().getRoleName()));
        this.userEmail.setText("Email: " + user.getEmail());
        this.userFiscalCode.setText("Fiscal code: " + user.getFiscalCode());
    }

    private void buildUserContractsPane(UserProfileViewModel viewModel) {
        this.userContracts.getChildren().clear();
        this.userContracts.getChildren().addAll(viewModel.getUserContracts().stream().map(contract -> {
            Card contractCard = ViewUtils.wrapNodeWithStyleClasses(new Card(), "contract-card");
            HBox deleteContractBox = ViewUtils.wrapNodeWithStyleClasses(new HBox(), "delete-contract-box");
            deleteContractBox.setAlignment(Pos.CENTER_RIGHT);
            ToggleButton deleteContractButton = ViewUtils.wrapNodeWithStyleClasses(new ToggleButton(), "delete-contract-button");
            deleteContractButton.setOnAction(e -> {
                viewModel.deleteUserContract(contract);
                this.buildUserContractsPane(viewModel);
            });
            deleteContractBox.getChildren().add(deleteContractButton);
            WrappableLabel contractAddress = ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Address: " + contract.getAddress()), "contract-label");
            String contractServicesString = contract.getServices().stream().map(svcType -> StringUtils.capitalize(svcType.getName())).collect(Collectors.joining(", "));
            WrappableLabel contractServices = ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Services: " + contractServicesString), "contract-label");
            String contractStartDateString = new SimpleDateFormat("yyyy/MM/dd").format(contract.getStartDate());
            WrappableLabel contractStartDate = ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Start date: " + contractStartDateString), "contract-label");
            contractCard.getChildren().addAll(deleteContractBox, contractAddress, contractServices, contractStartDate);
            return contractCard;
        }).collect(Collectors.toList()));
    }
}
