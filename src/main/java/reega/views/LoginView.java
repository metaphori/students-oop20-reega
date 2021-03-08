package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import reega.controllers.LoginController;
import reega.util.ValueResult;
import reega.viewutils.DialogFactory;

/**
 * Class for the Login View Component
 *
 * @author Marco
 *
 */
public class LoginView extends GridPane {

    @FXML
    private TextField emailOrFiscalCodeField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberMeCheckBox;

    private final LoginController loginController;

    public LoginView(final LoginController controller) {
        this.loginController = controller;
        final FXMLLoader fxmlLoader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        // Try the login without the password
        this.loginController.tryLoginWithoutPassword();

        // Change email or fiscal code on lost focus
        this.emailOrFiscalCodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.loginController.setEmailOrFiscalCode(this.emailOrFiscalCodeField.getText());
            }
        });
        // Change password on lost focus
        this.passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.loginController.setPassword(this.passwordField.getText());
            }
        });
    }

    /**
     * Jump to the registration page
     */
    @FXML
    private void jumpToRegistration() {
        this.loginController.jumpToRegistration();
    }

    /**
     * Login into the app
     */
    @FXML
    private void login() {
        final ValueResult<Void> valueResult = this.loginController.login(this.rememberMeCheckBox.isSelected());
        if (valueResult.isInvalid()) {
            DialogFactory.getInstance()
                    .buildAlert(AlertType.ERROR, "Login error", valueResult.getMessage())
                    .showAndWait();
        }
    }
}
