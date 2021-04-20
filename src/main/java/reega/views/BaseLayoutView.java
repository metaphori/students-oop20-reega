/**
 *
 */
package reega.views;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import reega.controllers.MasterViewModel;
import reega.viewcomponents.ContentControl;
import reega.viewutils.ReegaFXMLLoader;

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
        ReegaFXMLLoader.loadFXML(this, "views/BaseLayout.fxml");
        this.contentControl.objectProperty().bind(viewModel.getNavigator().selectedViewModelProperty());
        this.backArrowButton.visibleProperty().bind(viewModel.getNavigator().navigationStackNotEmptyProperty());
        this.backArrowButton.setOnAction(e -> viewModel.getNavigator().popController());
        this.backArrowButton.managedProperty().bind(this.backArrowButton.visibleProperty());
        viewModel.initializeApp();

    }

}
