package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import reega.controllers.RegistrationController;
import reega.util.ValueResult;
import reega.viewutils.DialogFactory;

/**
 * Class for the Registration View Component
 *
 * @author Marco
 *
 */
public class RegistrationView extends GridPane {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fiscalCodeField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private final RegistrationController registrationController;

    public RegistrationView(final RegistrationController registrationController) {
        this.registrationController = registrationController;
        final FXMLLoader fxmlLoader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/Registration.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        // Change name on lost focus
        this.nameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationController.setName(this.nameField.getText());
            }
        });
        // Change surname on lost focus
        this.surnameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationController.setSurname(this.surnameField.getText());
            }
        });
        // Change email on lost focus
        this.emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationController.setEmail(this.emailField.getText());
            }
        });
        // Change fiscal code on lost focus
        this.fiscalCodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationController.setFiscalCode(this.fiscalCodeField.getText());
            }
        });
        // Change password on lost focus
        this.passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationController.setPassword(this.passwordField.getText());
            }
        });
        // Change confirmPassword on lost focus
        this.confirmPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationController.setConfirmPassword(this.confirmPasswordField.getText());
            }
        });
    }

    @FXML
    private void jumpToLogin() {
        this.registrationController.jumpToLogin();
    }

    @FXML
    private void register() {
        final ValueResult<Void> result = this.registrationController.register();
        if (result.isInvalid()) {
            DialogFactory.getInstance()
                    .buildAlert(AlertType.ERROR, "Create user error", result.getMessage())
                    .showAndWait();
        }
    }

}
