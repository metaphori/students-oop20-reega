package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import reega.controllers.RegistrationControllerImpl;

/**
 * Class for the Registration View Component
 *
 * @author Marco
 *
 */
public class RegistrationView extends GridPane {

    private final RegistrationControllerImpl registrationController;

    public RegistrationView(final RegistrationControllerImpl registrationController) {
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
    }

    @FXML
    private void jumpToLogin() {
        this.registrationController.jumpToLogin();
    }

}
