package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import reega.controllers.LoginViewModel;
import reega.util.ValueResult;
import reega.viewcomponents.MaxWidthButton;
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
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink jumpToRegistrationHyperLink;

    public LoginView(final LoginViewModel viewModel) {
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
                viewModel.setEmailOrFiscalCode(this.emailOrFiscalCodeField.getText());
            }
        });
        // Change password on lost focus
        this.passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                viewModel.setPassword(this.passwordField.getText());
            }
        });

        this.emailOrFiscalCodeField.setOnAction(e -> this.passwordField.requestFocus());

        this.passwordField.setOnAction(e -> {
            viewModel.setPassword(this.passwordField.getText());
            this.login(viewModel);
        });

        this.loginButton.setOnAction(e -> {
            this.login(viewModel);
        });

        this.jumpToRegistrationHyperLink.setOnAction(e -> {
            viewModel.jumpToRegistration();
        });


    }

    /**
     * Execute the login
     * @param loginViewModel viewmodel used for the login
     */
    private void login(LoginViewModel loginViewModel) {
        final ValueResult<Void> valueResult = loginViewModel.login(this.rememberMeCheckBox.isSelected());
        if (valueResult.isInvalid()) {
            DialogFactory.buildAlert(AlertType.ERROR, "Login error", valueResult.getMessage())
                    .showAndWait();
        }
    }
}
