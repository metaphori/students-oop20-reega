package reega.views;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import reega.controllers.UserProfileController;
import reega.users.User;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewutils.ViewUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class UserProfileView extends VBox {

    @FXML
    private Label userName;
    @FXML
    private Label userSurname;
    @FXML
    private Label userRole;
    @FXML
    private Label userEmail;
    @FXML
    private Label userFiscalCode;
    @FXML
    private FlexibleGridPane userContracts;

    public UserProfileView(UserProfileController controller) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/UserProfile.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }


        if (controller.user().isNotNull().get()) {
            this.setUserProperties(controller.getUser(), controller);
        }
        controller.user().addListener((observable, oldValue, newValue) -> {
            this.setUserProperties(newValue, controller);
        });
    }

    private void setUserProperties(User user, UserProfileController controller) {
        this.userName.setText("Name: " + StringUtils.capitalize(user.getName()));
        this.userSurname.setText("Surname: " + StringUtils.capitalize(user.getSurname()));
        this.userRole.setText("Role: " + StringUtils.capitalize(user.getRole().getRoleName()));
        this.userEmail.setText("Email: " + user.getEmail());
        this.userFiscalCode.setText("Fiscal code: " + user.getFiscalCode());
        this.userContracts.getChildren().clear();
        this.userContracts.getChildren().addAll(controller.getUserContracts().stream().map(contract -> {
            Card contractCard = ViewUtils.wrapNodeWithStyleClasses(new Card(), "contract-card");
            Label contractAddress = ViewUtils.wrapNodeWithStyleClasses(new Label("Address: " + contract.getAddress()), "contract-label");
            String contractServicesString = contract.getServices().stream().map(svcType -> StringUtils.capitalize(svcType.getName())).collect(Collectors.joining(new StringBuffer(",")));
            Label contractServices = ViewUtils.wrapNodeWithStyleClasses(new Label("Services: " + contractServicesString), "contract-label");
            String contractStartDateString = new SimpleDateFormat("yyyy/MM/dd").format(contract.getStartDate());
            Label contractStartDate = ViewUtils.wrapNodeWithStyleClasses(new Label("Start date: " + contractStartDateString), "contract-label");
            contractCard.getChildren().addAll(contractAddress, contractServices, contractStartDate);
            return contractCard;
        }).collect(Collectors.toList()));
    }
}
