/**
 *
 */
package reega.viewutils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 * @author Marco
 *
 */
public class DialogFactory {

    private static DialogFactory instance;

    /**
     * Get the static instance of a {@link DialogFactory}
     *
     * @return the static instance of a {@link DialogFactory}
     */
    public synchronized static DialogFactory getInstance() {
        if (DialogFactory.instance == null) {
            DialogFactory.instance = new DialogFactory();
        }
        return DialogFactory.instance;
    }

    /**
     * Build an alert
     *
     * @param alertType type of the alert
     * @param title     title of the alert
     * @param content   string content of the alert
     * @param buttons   buttons to show
     * @return an {@link Alert} made up of these properties
     * @see AlertType
     * @see Alert
     */
    public Alert buildAlert(final AlertType alertType, final String title, final String content,
            final ButtonType... buttons) {
        final Alert alert = new Alert(alertType, content, buttons);
        alert.setGraphic(null);
        alert.setHeaderText(title);
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(ClassLoader.getSystemClassLoader().getResource("css/Dialog.css").toExternalForm());
        dialogPane.getStylesheets()
                .add(ClassLoader.getSystemClassLoader().getResource("css/Common.css").toExternalForm());
        dialogPane.getStyleClass().add("commonDialog");
        return alert;
    }

}
