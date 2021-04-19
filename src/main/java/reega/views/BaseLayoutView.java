/**
 *
 */
package reega.views;

import java.io.IOException;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import reega.controllers.MasterViewModel;
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

    @Inject
    public BaseLayoutView(final MasterViewModel viewModel) {
        final FXMLLoader fxmlLoader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/BaseLayout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.contentControl.objectProperty().bind(viewModel.getNavigator().selectedViewModelProperty());
        this.backArrowButton.visibleProperty().bind(viewModel.getNavigator().navigationStackNotEmptyProperty());
        this.backArrowButton.setOnAction(e -> viewModel.getNavigator().popController());
        this.backArrowButton.managedProperty().bind(this.backArrowButton.visibleProperty());
        viewModel.initializeApp();

    }

}
