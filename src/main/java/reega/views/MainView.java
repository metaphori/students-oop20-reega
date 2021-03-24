package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import reega.controllers.MainController;

public class MainView extends HBox {

    @FXML
    private Label userEmail;

    public MainView(final MainController controller) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/Main.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if (controller.user().isNotNull().get()) {
            this.userEmail.setText(controller.user().get().getEmail());
        }
        controller.user().addListener((observable, oldValue, newValue) -> {
            this.userEmail.setText(newValue.getEmail());
        });
    }
}
