/**
 *
 */
package reega.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import reega.controllers.MainController;
import reega.viewutils.ContentControl;

/**
 * @author Marco
 *
 */
public class BaseLayoutView extends ScrollPane implements Initializable {

    @FXML
    private ContentControl contentControl;

    private final MainController controller;

    @Inject
    public BaseLayoutView(final MainController controller) {
        final FXMLLoader fxmlLoader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/BaseLayout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.controller = controller;
        this.controller.initializeApp();
        this.contentControl.objectProperty().bind(this.controller.getNavigator().selectedControllerProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

    }

}
