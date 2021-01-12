/**
 *
 */
package reega.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reega.util.ServiceProvider;

/**
 * @author Marco
 *
 */
public class ReegaMain extends Application {

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
        AppInitializer.instance.initializateTemplateManager();
        final ServiceProvider svcProvider = AppInitializer.instance.buildServiceProvider();
        primaryStage.setScene(new Scene(svcProvider.getRequiredService(BaseLayoutView.class)));
        primaryStage.show();
    }
}
