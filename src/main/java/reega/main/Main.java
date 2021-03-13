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
        primaryStage.setScene(new Scene(svcProvider.getRequiredService(BaseLayoutView.class)));
        primaryStage.show();
    }
}
