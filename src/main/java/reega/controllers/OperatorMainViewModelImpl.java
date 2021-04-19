package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.data.OperatorContractManager;
import reega.data.OperatorDataFetcher;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.DataPlotter;
import reega.statistics.StatisticsController;
import reega.users.User;
import reega.viewutils.Command;
import reega.viewutils.LabeledCommand;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OperatorMainViewModelImpl extends MainViewModelImpl implements OperatorMainViewModel {
        private ObjectProperty<User> selectedUserProperty = new SimpleObjectProperty<>();
        private List<Command> defaultCommands;

        @Inject public OperatorMainViewModelImpl(final StatisticsController statisticsController,
                                                 final DataPlotter dataPlotter, final ExceptionHandler exceptionHandler,
                                                 final OperatorDataFetcher dataFetcher, final OperatorContractManager contractManager) {
                super(statisticsController, dataPlotter, exceptionHandler, dataFetcher, contractManager);
        }

        @Override protected OperatorContractManager getContractManager() {
                return (OperatorContractManager) super.getContractManager();
        }

        @Override protected OperatorDataFetcher getDataFetcher() {
                return (OperatorDataFetcher) super.getDataFetcher();
        }

        @Override protected void initializeCommands() {
                super.initializeCommands();
                this.getCommands().add(new LabeledCommand("Search", (args) -> {
                        this.jumpToSearchUser();
                }));
                this.defaultCommands = List.copyOf(this.getCommands());
        }

        @Override protected void initializeStatistics(final User user) {
                List<Data> initialData = this.getDataFetcher().getGeneralData();
                this.getStatisticsController().setData(initialData);
        }

        @Override public void jumpToSearchUser() {
                this.pushViewModel(SearchUserViewModel.class, searchUserController -> {
                        searchUserController.setUserFoundEventHandler(evtArgs -> {
                                if (evtArgs != null && evtArgs.getEventItem() != null) {
                                        /**
                                         * Pop the {@link SearchUserViewModel}
                                         */
                                        this.popViewModel();
                                        User user = evtArgs.getEventItem();
                                        this.initializeStatisticsForSelectedUser(user, null);
                                }
                        });
                        searchUserController.setContractFoundEventHandler(evtArgs -> {
                                if (evtArgs != null && evtArgs.getEventItem() != null) {
                                        /**
                                         * Pop the {@link SearchUserViewModel}
                                         */
                                        this.popViewModel();
                                        User user = evtArgs.getEventItem().getKey();
                                        this.initializeStatisticsForSelectedUser(user,
                                                List.of(evtArgs.getEventItem().getValue()));
                                }
                        });
                }, false);
        }

        /**
         * Initialize the statistics when a new user is selected
         *
         * @param newUser new user
         */
        private void initializeStatisticsForSelectedUser(final User newUser, final List<Contract> selectedContracts) {
                final List<Contract> allContracts = this.getContractManager().fetchContractsByUser(newUser);
                this.setUserContracts(allContracts);
                this.getSelectedContracts().clear();
                List<Contract> contracts = selectedContracts == null ? allContracts : selectedContracts;
                this.getSelectedContracts().addAll(contracts);
                List<Data> initialData = this.getDataFetcher().fetchAllUserData(newUser, contracts);
                this.getStatisticsController().setData(initialData);

                this.getCommands().clear();
                this.getCommands().addAll(this.defaultCommands);
                this.getCommands()
                        .addAll(new LabeledCommand("Remove current selection", args -> this.removeSelectedUser()),
                                new LabeledCommand("See selected user profile",
                                        args -> this.pushViewModel(UserProfileViewModel.class,
                                                userProfileController -> {
                                                        userProfileController.setUserContracts(allContracts);
                                                        userProfileController.setUser(newUser);
                                                        userProfileController.setDeleteUserContractHandler(evtArgs -> {
                                                                Contract contractToDelete = evtArgs.getEventItem();
                                                                if (this.getContractManager()
                                                                        .deleteUserContract(contractToDelete)) {
                                                                        this.getContracts().remove(contractToDelete);
                                                                        this.removeSelectedContract(contractToDelete);
                                                                }
                                                        });
                                                }, false)),
                                new LabeledCommand("Add contract to the selected user", args -> {
                                        this.pushViewModel(ContractCreationViewModel.class,
                                                contractCreationController -> {
                                                        contractCreationController.setUser(newUser);
                                                        contractCreationController.setContractCreateEventHandler(
                                                                evtArgs -> {
                                                                        this.getContracts().add(evtArgs.getEventItem());
                                                                        this.popViewModel();
                                                                });
                                                }, false);
                                }), new LabeledCommand("History", args -> this.pushViewModel(HistoryViewModel.class,
                                        controller -> controller.setContracts(this.getSelectedContracts()), false)));

                this.setSelectedUser(newUser);
        }

        /**
         * Set the selected user
         *
         * @param newUser user that needs to be marked as selected
         */
        private void setSelectedUser(final User newUser) {
                this.selectedUser().set(newUser);
        }

        @Override public Set<ServiceType> getAvailableServiceTypes() {
                return this.getSelectedUser()
                        .map(elem -> super.getAvailableServiceTypes())
                        .orElse(Arrays.stream(ServiceType.values()).collect(Collectors.toUnmodifiableSet()));
        }

        @Override public Optional<User> getSelectedUser() {
                return Optional.ofNullable(this.selectedUser().get());
        }

        @Override public ObjectProperty<User> selectedUser() {
                return this.selectedUserProperty;
        }

        @Override public void removeSelectedUser() {
                this.initializeStatistics(this.getUser());
                this.getContracts().clear();
                this.getSelectedContracts().clear();
                this.selectedUser().set(null);
                this.getCommands().clear();
                this.getCommands().addAll(this.defaultCommands);
        }
}