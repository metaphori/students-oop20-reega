/**
 *
 */
package reega.viewutils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import reega.io.JavaFXSaveDialog;
import reega.io.SaveDialog;

public final class DialogFactory {
    private DialogFactory() {
    }

    /**
     * Build an alert.
     *
     * @param alertType type of the alert
     * @param title     title of the alert
     * @param content   string content of the alert
     * @param buttons   buttons to show
     * @return an {@link Alert} made up of these properties
     * @see AlertType
     * @see Alert
     */
    public static Alert buildAlert(final AlertType alertType, final String title, final String content,
            final ButtonType... buttons) {
        final Alert alert = new Alert(alertType, content, buttons);
        alert.setGraphic(null);
        alert.setHeaderText(title);
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(ClassLoader.getSystemClassLoader().getResource("css/Dialog.css").toExternalForm());
        dialogPane.getStylesheets()
                .add(ClassLoader.getSystemClassLoader().getResource("css/Common.css").toExternalForm());
        dialogPane.getStylesheets()
                .add(ClassLoader.getSystemClassLoader().getResource("css/Button.css").toExternalForm());
        dialogPane.getStyleClass().add("commonDialog");
        return alert;
    }

    /**
     * Get the default {@link SaveDialog}.
     *
     * @return the default {@link SaveDialog} implementation
     */
    public static SaveDialog getDefaultSaveDialog() {
        return new JavaFXSaveDialog();
    }

}
