package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import reega.controllers.LoginViewModel;
import reega.util.ValueResult;
import reega.viewutils.DialogFactory;
import reega.viewutils.ReegaFXMLLoader;

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

    private final LoginViewModel loginViewModel;

    public LoginView(final LoginViewModel viewModel) {
        this.loginViewModel = viewModel;
        ReegaFXMLLoader.loadFXML(this, "views/Login.fxml");

        ValueResult<Void> tryLoginResult = viewModel.tryLogin();
        if (tryLoginResult.isInvalid()) {
            DialogFactory.buildAlert(AlertType.ERROR,
                    "Error when trying to login with the token",
                    viewModel.tryLogin().getMessage(),
                    ButtonType.CLOSE).showAndWait();
        }

        // Change email or fiscal code on lost focus
        this.emailOrFiscalCodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.loginViewModel.setEmailOrFiscalCode(this.emailOrFiscalCodeField.getText());
            }
        });
        // Change password on lost focus
        this.passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.loginViewModel.setPassword(this.passwordField.getText());
            }
        });

        this.emailOrFiscalCodeField.setOnAction((e) -> this.passwordField.requestFocus());

        this.passwordField.setOnAction((e) -> {
            this.loginViewModel.setPassword(this.passwordField.getText());
            this.login();
        });
    }

    /**
     * Jump to the registration page
     */
    @FXML
    private void jumpToRegistration() {
        this.loginViewModel.jumpToRegistration();
    }

    /**
     * Login into the app
     */
    @FXML
    private void login() {
        final ValueResult<Void> valueResult = this.loginViewModel.login(this.rememberMeCheckBox.isSelected());
        if (valueResult.isInvalid()) {
            DialogFactory.buildAlert(AlertType.ERROR, "Login error", valueResult.getMessage())
                    .showAndWait();
        }
    }
}
