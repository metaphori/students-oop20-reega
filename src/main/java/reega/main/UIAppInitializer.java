/**
 *
 */
package reega.main;

import java.sql.SQLException;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.scene.Parent;
import reega.auth.AuthManager;
import reega.auth.RemindableAuthManager;
import reega.controllers.LoginController;
import reega.controllers.LoginControllerImpl;
import reega.controllers.MainController;
import reega.controllers.RegistrationController;
import reega.controllers.RegistrationControllerImpl;
import reega.data.AuthController;
import reega.data.AuthControllerFactory;
import reega.io.IOController;
import reega.io.IOControllerFactory;
import reega.io.TokenIOController;
import reega.logging.ExceptionHandler;
import reega.logging.SimpleExceptionHandler;
import reega.util.ServiceCollection;
import reega.util.ServiceProvider;
import reega.views.LoginView;
import reega.views.RegistrationView;
import reega.viewutils.DataTemplate;
import reega.viewutils.DataTemplateManager;
import reega.viewutils.Navigator;
import reega.viewutils.NavigatorImpl;

/**
 * App initializer for the UI main class
 *
 * @author Marco
 *
 */
public class UIAppInitializer implements AppInitializer {

    private ServiceProvider svcProvider;
    private boolean alreadyCalledInitialize;

    private UIAppInitializer() {

    }

    private static UIAppInitializer instance;

    /**
     * Get the only instance of the UI app initializer
     *
     * @return the only instance of the UI app initializer
     */
    public static synchronized UIAppInitializer getInstance() {
        if (UIAppInitializer.instance == null) {
            UIAppInitializer.instance = new UIAppInitializer();
        }
        return UIAppInitializer.instance;
    }

    /**
     * Build the service provider for the app
     *
     * @return the service provider for the app
     * @throws ClassNotFoundException exception raised in
     *                                {@link AuthControllerFactory#getDefaultAuthController()}
     * @throws SQLException           exception raised in
     *                                {@link AuthControllerFactory#getDefaultAuthController()}
     */
    private ServiceProvider buildServiceProvider() throws ClassNotFoundException, SQLException {
        final ServiceCollection svcCollection = new ServiceCollection();
        svcCollection.addSingleton(Navigator.class, (Function<ServiceProvider, Navigator>) NavigatorImpl::new);
        svcCollection.addSingleton(MainController.class);
        svcCollection.addSingleton(AuthController.class, AuthControllerFactory.getDefaultAuthController());
        svcCollection.addSingleton(IOController.class, IOControllerFactory.getDefaultIOController());
        svcCollection.addSingleton(TokenIOController.class, IOControllerFactory.getDefaultTokenIOController());
        svcCollection.addSingleton(ExceptionHandler.class, SimpleExceptionHandler.class);
        svcCollection.addSingleton(AuthManager.class, RemindableAuthManager.class);
        svcCollection.addTransient(LoginController.class, LoginControllerImpl.class);
        svcCollection.addTransient(RegistrationController.class, RegistrationControllerImpl.class);
        svcCollection.addSingleton(BaseLayoutView.class);
        return svcCollection.buildServiceProvider();
    }

    /**
     * Initialize the static template manager
     */
    private void initializeTemplateManager() {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() throws Exception {
        if (this.alreadyCalledInitialize) {
            throw new IllegalStateException("The initialize() method has already been created");
        }
        this.initializeTemplateManager();
        this.svcProvider = this.buildServiceProvider();
        this.alreadyCalledInitialize = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceProvider getServiceProvider() {
        return this.svcProvider;
    }

}
