package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import reega.controllers.RegistrationViewModel;
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

    private final RegistrationViewModel registrationViewModel;

    public RegistrationView(final RegistrationViewModel registrationViewModel) {
        this.registrationViewModel = registrationViewModel;
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
                this.registrationViewModel.setName(this.nameField.getText());
            }
        });
        // Change surname on lost focus
        this.surnameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationViewModel.setSurname(this.surnameField.getText());
            }
        });
        // Change email on lost focus
        this.emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationViewModel.setEmail(this.emailField.getText());
            }
        });
        // Change fiscal code on lost focus
        this.fiscalCodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationViewModel.setFiscalCode(this.fiscalCodeField.getText());
            }
        });
        // Change password on lost focus
        this.passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationViewModel.setPassword(this.passwordField.getText());
            }
        });
        // Change confirmPassword on lost focus
        this.confirmPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.registrationViewModel.setConfirmPassword(this.confirmPasswordField.getText());
            }
        });
    }

    @FXML
    private void jumpToLogin() {
        this.registrationViewModel.jumpToLogin();
    }

    @FXML
    private void register() {
        final ValueResult<Void> result = this.registrationViewModel.register();
        if (result.isInvalid()) {
            DialogFactory.buildAlert(AlertType.ERROR, "Create user error", result.getMessage())
                    .showAndWait();
        }
    }

}
