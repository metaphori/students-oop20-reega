package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import reega.controllers.RegistrationViewModel;
import reega.util.ValueResult;
import reega.viewutils.DialogFactory;
import reega.viewutils.ReegaFXMLLoader;

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
    @FXML
    private Button registerButton;
    @FXML
    private Hyperlink jumpToLoginHyperlink;

    public RegistrationView(final RegistrationViewModel registrationViewModel) {
        ReegaFXMLLoader.loadFXML(this, "views/Registration.fxml");

        // Change name on lost focus
        this.nameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrationViewModel.setName(this.nameField.getText());
            }
        });
        // Change surname on lost focus
        this.surnameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrationViewModel.setSurname(this.surnameField.getText());
            }
        });
        // Change email on lost focus
        this.emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrationViewModel.setEmail(this.emailField.getText());
            }
        });
        // Change fiscal code on lost focus
        this.fiscalCodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrationViewModel.setFiscalCode(this.fiscalCodeField.getText());
            }
        });
        // Change password on lost focus
        this.passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrationViewModel.setPassword(this.passwordField.getText());
            }
        });
        // Change confirmPassword on lost focus
        this.confirmPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrationViewModel.setConfirmPassword(this.confirmPasswordField.getText());
            }
        });

        this.jumpToLoginHyperlink.setOnAction(e -> registrationViewModel.jumpToLogin());
        this.registerButton.setOnAction(e -> this.register(registrationViewModel));
    }

    /**
     * Register into the Reega platform
     * @param registrationViewModel viewmodel used for the registration
     */
    private void register(RegistrationViewModel registrationViewModel) {
        final ValueResult<Void> result = registrationViewModel.register();
        if (result.isInvalid()) {
            DialogFactory.buildAlert(AlertType.ERROR, "Create user error", result.getMessage())
                    .showAndWait();
        }
    }

}
