package reega.viewutils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * FXML Loader for the REEGA platform.
 */
public final class ReegaFXMLLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReegaFXMLLoader.class);

    private ReegaFXMLLoader() {
    }

    /**
     * Load the local FXML file specified by the <code>fxmlFilePath</code> and set the root and controller of the FXML
     * to the <code>root</code>.
     *
     * @param root         root node
     * @param fxmlFilePath local FXML file path
     */
    public static void loadFXML(final Node root, final String fxmlFilePath) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource(fxmlFilePath));

        loader.setRoot(root);
        loader.setController(root);

        try {
            loader.load();
        } catch (final IOException e) {
            ReegaFXMLLoader.LOGGER.trace("Exception occurred loading the FXML for the Node: " + root, e);
        }
    }
}
