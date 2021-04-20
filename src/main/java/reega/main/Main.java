/**
 *
 */
package reega.main;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import reega.util.ServiceProvider;
import reega.views.BaseLayoutView;

/**
 *
 */
public class Main extends Application {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        UIAppInitializer.getInstance().initialize();
        final ServiceProvider svcProvider = UIAppInitializer.getInstance().getServiceProvider();
        final Screen primaryScreen = Screen.getPrimary();
        final double aspectRatio = this.getScreenAspectRatio(primaryScreen);
        final Rectangle2D bounds = primaryScreen.getVisualBounds();
        // Use the 40% of the width
        final double minWidth = bounds.getWidth() * 0.4;
        // Keep the aspect ratio
        final double minHeight = minWidth / aspectRatio;
        primaryStage.setMinHeight(minHeight);
        primaryStage.setMinWidth(minWidth);

        // Use the 70% of the width
        final double prefWidth = bounds.getWidth() * 0.7;
        // Keep the aspect ratio
        final double prefHeight = prefWidth / aspectRatio;
        primaryStage.setHeight(prefHeight);
        primaryStage.setWidth(prefWidth);

        primaryStage.getIcons()
                .addAll(new Image("icons/Reega_Icon16x16.png"), new Image("icons/Reega_Icon24x24.png"),
                        new Image("icons/Reega_Icon32x32.png"), new Image("icons/Reega_Icon48x48.png"),
                        new Image("icons/Reega_Icon64x64.png"), new Image("icons/Reega_Icon128x128.png"),
                        new Image("icons/Reega_Icon256x256.png"));

        primaryStage.setScene(new Scene(svcProvider.getRequiredService(BaseLayoutView.class)));
        primaryStage.show();
    }

    private double getScreenAspectRatio(final Screen screen) {
        final Rectangle2D bounds = screen.getBounds();
        return bounds.getWidth() / bounds.getHeight();
    }
}
