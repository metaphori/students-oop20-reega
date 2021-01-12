/**
 *
 */
package reega.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import reega.controllers.LoginController;
import reega.controllers.LoginControllerImpl;
import reega.controllers.MainController;
import reega.controllers.RegistrationController;
import reega.controllers.RegistrationControllerImpl;
import reega.util.ServiceCollection;
import reega.util.ServiceProvider;
import reega.views.LoginView;
import reega.views.RegistrationView;
import reega.viewutils.ContentControl;
import reega.viewutils.DataTemplate;
import reega.viewutils.DataTemplateManager;
import reega.viewutils.Navigator;
import reega.viewutils.NavigatorImpl;

/**
 * @author Marco
 *
 */
public class BaseLayoutView extends ScrollPane implements Initializable {

    @FXML
    private ContentControl contentControl;

    private Navigator navigator;

    public BaseLayoutView() {
        final FXMLLoader fxmlLoader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/BaseLayout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.initializeNavigator();
        this.contentControl.objectProperty().bind(this.navigator.selectedControllerProperty());
        this.navigator.pushController(LoginController.class);
    }

    private void initializeNavigator() {
        final ServiceProvider svcProvider = this.buildServiceProvider();
        final DataTemplateManager templateManager = DataTemplateManager.instance;
        templateManager.addTemplate(new DataTemplate<RegistrationControllerImpl>() {
            @Override
            public Class<RegistrationControllerImpl> getDataObjectClass() {
                return RegistrationControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(final RegistrationControllerImpl controller) {
                return () -> new RegistrationView(controller);
            }
        });
        templateManager.addTemplate(new DataTemplate<LoginControllerImpl>() {
            @Override
            public Class<LoginControllerImpl> getDataObjectClass() {
                return LoginControllerImpl.class;
            }

            @Override
            public Supplier<? extends Parent> getControlFactory(final LoginControllerImpl controller) {
                return () -> new LoginView(controller);
            }
        });
        this.navigator = svcProvider.getRequiredService(Navigator.class);
    }

    private ServiceProvider buildServiceProvider() {
        final ServiceCollection svcCollection = new ServiceCollection();
        svcCollection.addSingleton(Navigator.class, (Function<ServiceProvider, Navigator>) NavigatorImpl::new);
        svcCollection.addSingleton(MainController.class);
        svcCollection.addTransient(LoginController.class, LoginControllerImpl.class);
        svcCollection.addTransient(RegistrationController.class, RegistrationControllerImpl.class);
        return svcCollection.buildServiceProvider();
    }

}
