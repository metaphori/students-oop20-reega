/**
 *
 */
package reega.main;

import java.util.function.Function;
import java.util.function.Supplier;

import javafx.scene.Parent;
import reega.controllers.LoginController;
import reega.controllers.LoginControllerImpl;
import reega.controllers.MainController;
import reega.controllers.RegistrationController;
import reega.controllers.RegistrationControllerImpl;
import reega.util.ServiceCollection;
import reega.util.ServiceProvider;
import reega.views.LoginView;
import reega.views.RegistrationView;
import reega.viewutils.DataTemplate;
import reega.viewutils.DataTemplateManager;
import reega.viewutils.Navigator;
import reega.viewutils.NavigatorImpl;

/**
 * @author Marco
 *
 */
public class AppInitializer {
    private AppInitializer() {
    }

    public static AppInitializer instance = new AppInitializer();

    public ServiceProvider buildServiceProvider() {
        final ServiceCollection svcCollection = new ServiceCollection();
        svcCollection.addSingleton(Navigator.class, (Function<ServiceProvider, Navigator>) NavigatorImpl::new);
        svcCollection.addSingleton(MainController.class);
        svcCollection.addTransient(LoginController.class, LoginControllerImpl.class);
        svcCollection.addTransient(RegistrationController.class, RegistrationControllerImpl.class);
        svcCollection.addSingleton(MainController.class);
        svcCollection.addSingleton(BaseLayoutView.class);
        return svcCollection.buildServiceProvider();
    }

    public void initializateTemplateManager() {
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
    }

}
