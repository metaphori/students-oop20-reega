package reega.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import reega.controllers.MainViewController;

public class MainView extends GridPane {

    @FXML
    private Label testText;

    public MainView(final MainViewController controller) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/Main.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if (controller.user().isNotNull().get()) {
            this.testText.setText(controller.user().get().getEmail());
        }
        controller.user().addListener((observable, oldValue, newValue) -> {
            this.testText.setText(newValue.getEmail());
        });
    }
}
