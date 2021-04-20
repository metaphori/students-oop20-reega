/**
 *
 */
package reega.main;

import javafx.scene.Parent;
import reega.auth.AuthManager;
import reega.auth.RemindableAuthManager;
import reega.controllers.*;
import reega.data.*;
import reega.data.factory.AuthControllerFactory;
import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
import reega.data.factory.UserControllerFactory;
import reega.data.remote.RemoteConnection;
import reega.io.IOController;
import reega.io.IOControllerFactory;
import reega.io.TokenIOController;
import reega.logging.ExceptionHandler;
import reega.logging.SimpleExceptionHandler;
import reega.statistics.DataPlotter;
import reega.statistics.DataPlotterImpl;
import reega.statistics.StatisticsController;
import reega.statistics.StatisticsControllerImpl;
import reega.util.ServiceCollection;
import reega.util.ServiceProvider;
import reega.views.*;
import reega.viewutils.*;

import java.util.function.Function;
import java.util.function.Supplier;

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
         */
        private ServiceProvider buildServiceProvider() {
                final ServiceCollection svcCollection = new ServiceCollection();
                svcCollection.addSingleton(Navigator.class, (Function<ServiceProvider, Navigator>) NavigatorImpl::new);
                svcCollection.addSingleton(MasterViewModel.class);
                svcCollection.addSingleton(AuthController.class, AuthControllerFactory.getDefaultAuthController(null));
                svcCollection.addSingleton(IOController.class, IOControllerFactory.getDefaultIOController());
                svcCollection.addSingleton(TokenIOController.class, IOControllerFactory.getDefaultTokenIOController());
                svcCollection.addSingleton(ContractController.class,
                        ContractControllerFactory.getDefaultDataController(new RemoteConnection()));
                svcCollection.addSingleton(DataController.class,
                        DataControllerFactory.getDefaultDataController(new RemoteConnection()));
                svcCollection.addSingleton(ExceptionHandler.class, SimpleExceptionHandler.class);
                svcCollection.addSingleton(reega.data.UserController.class,
                        UserControllerFactory.getDefaultUserController(null));
                svcCollection.addSingleton(AuthManager.class, RemindableAuthManager.class);
                svcCollection.addSingleton(DataFetcher.class, DataFetcherImpl.class);
                svcCollection.addSingleton(OperatorDataFetcher.class, OperatorDataFetcherImpl.class);
                svcCollection.addSingleton(ContractManager.class, ContractManagerImpl.class);
                svcCollection.addSingleton(OperatorContractManager.class, OperatorContractManagerImpl.class);
                svcCollection.addTransient(StatisticsController.class, StatisticsControllerImpl.class);
                svcCollection.addTransient(DataPlotter.class, DataPlotterImpl.class);
                svcCollection.addTransient(LoginViewModel.class, LoginViewModelImpl.class);
                svcCollection.addTransient(RegistrationViewModel.class, RegistrationViewModelImpl.class);
                svcCollection.addTransient(MainViewModel.class, (svcProvider) -> {
                        final StatisticsController statisticsController = svcProvider.getRequiredService(
                                StatisticsController.class);
                        final DataPlotter dataPlotter = svcProvider.getRequiredService(DataPlotter.class);
                        final ExceptionHandler exceptionHandler = svcProvider.getRequiredService(
                                ExceptionHandler.class);
                        final DataFetcher dataFetcher = svcProvider.getRequiredService(DataFetcher.class);
                        final ContractManager contractManager = svcProvider.getRequiredService(ContractManager.class);

                        dataPlotter.setStatisticController(statisticsController);

                        return new MainViewModelImpl(statisticsController, dataPlotter, exceptionHandler, dataFetcher,
                                contractManager);
                });
                svcCollection.addTransient(OperatorMainViewModel.class, (svcProvider) -> {
                        final StatisticsController statisticsController = svcProvider.getRequiredService(
                                StatisticsController.class);
                        final DataPlotter dataPlotter = svcProvider.getRequiredService(DataPlotter.class);
                        final ExceptionHandler exceptionHandler = svcProvider.getRequiredService(
                                ExceptionHandler.class);
                        final OperatorDataFetcher dataFetcher = svcProvider.getRequiredService(
                                OperatorDataFetcher.class);
                        final OperatorContractManager contractFetcher = svcProvider.getRequiredService(
                                OperatorContractManager.class);

                        dataPlotter.setStatisticController(statisticsController);

                        return new OperatorMainViewModelImpl(statisticsController, dataPlotter, exceptionHandler,
                                dataFetcher, contractFetcher);
                });
                svcCollection.addTransient(SearchUserViewModel.class, SearchUserViewModelImpl.class);
                svcCollection.addTransient(ContractCreationViewModel.class, ContractCreationViewModelImpl.class);
                svcCollection.addTransient(UserProfileViewModel.class, UserProfileViewModelImpl.class);
                svcCollection.addTransient(HistoryViewModel.class, HistoryViewModelImpl.class);
                svcCollection.addSingleton(BaseLayoutView.class);
                return svcCollection.buildServiceProvider();
        }

        /**
         * Initialize the static template manager
         */
        private void initializeTemplateManager() {
                final DataTemplateManager templateManager = DataTemplateManager.instance;
                templateManager.addTemplate(new DataTemplate<RegistrationViewModelImpl>() {
                        @Override public Class<RegistrationViewModelImpl> getDataObjectClass() {
                                return RegistrationViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                final RegistrationViewModelImpl controller) {
                                return () -> new RegistrationView(controller);
                        }
                });
                templateManager.addTemplate(new DataTemplate<LoginViewModelImpl>() {
                        @Override public Class<LoginViewModelImpl> getDataObjectClass() {
                                return LoginViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                final LoginViewModelImpl controller) {
                                return () -> new LoginView(controller);
                        }
                });
                templateManager.addTemplate(new DataTemplate<MainViewModelImpl>() {
                        @Override public Class<MainViewModelImpl> getDataObjectClass() {
                                return MainViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                final MainViewModelImpl controller) {
                                return () -> new UserMainView(controller);
                        }
                });
                templateManager.addTemplate(new DataTemplate<OperatorMainViewModelImpl>() {

                        @Override public Class<OperatorMainViewModelImpl> getDataObjectClass() {
                                return OperatorMainViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                OperatorMainViewModelImpl controller) {
                                return () -> new OperatorMainView(controller);
                        }

                });
                templateManager.addTemplate(new DataTemplate<SearchUserViewModelImpl>() {

                        @Override public Class<SearchUserViewModelImpl> getDataObjectClass() {
                                return SearchUserViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                SearchUserViewModelImpl controller) {
                                return () -> new UserSearchView(controller);
                        }
                });
                templateManager.addTemplate(new DataTemplate<UserProfileViewModelImpl>() {

                        @Override public Class<UserProfileViewModelImpl> getDataObjectClass() {
                                return UserProfileViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                UserProfileViewModelImpl controller) {
                                return () -> new UserProfileView(controller);
                        }
                });

                templateManager.addTemplate(new DataTemplate<HistoryViewModelImpl>() {
                        @Override public Class<HistoryViewModelImpl> getDataObjectClass() {
                                return HistoryViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(HistoryViewModelImpl controller) {
                                return () -> new HistoryView(controller);
                        }
                });
                templateManager.addTemplate(new DataTemplate<ContractCreationViewModelImpl>() {

                        @Override public Class<ContractCreationViewModelImpl> getDataObjectClass() {
                                return ContractCreationViewModelImpl.class;
                        }

                        @Override public Supplier<? extends ReegaView> getControlFactory(
                                ContractCreationViewModelImpl controller) {
                                return () -> new ContractCreationView(controller);
                        }
                });
        }

        /**
         * {@inheritDoc}
         */
        @Override public void initialize() {
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
        @Override public ServiceProvider getServiceProvider() {
                return this.svcProvider;
        }

}
