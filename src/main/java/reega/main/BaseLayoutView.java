/**
 *
 */
package reega.main;

import java.io.IOException;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import reega.controllers.MasterController;
import reega.viewutils.ContentControl;

/**
 * @author Marco
 *
 */
public class BaseLayoutView extends ScrollPane {

    @FXML
    private ContentControl contentControl;
    @FXML
    private Button backArrowButton;

    private final MasterController controller;

    @Inject
    public BaseLayoutView(final MasterController controller) {
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
        this.backArrowButton.visibleProperty().bind(this.controller.getNavigator().navigationStackNotEmptyProperty());
    }

    @FXML
    private void popController() {
        this.controller.popController();
    }

}
