package reega.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import reega.controllers.MainViewController;

import java.io.IOException;

public class MainView extends GridPane {

    @FXML
    private Label testText;

    public MainView(final MainViewController controller) {
        final FXMLLoader loader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/Main.fxml")
        );

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.user().addListener((observable, oldValue, newValue) -> {
            testText.setText(newValue.getEmail());
        });
    }
}
