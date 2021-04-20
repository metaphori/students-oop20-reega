package reega.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

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

public class OperatorMainViewModelImpl extends MainViewModelImpl implements OperatorMainViewModel {
    private final ObjectProperty<User> selectedUserProperty = new SimpleObjectProperty<>();
    private List<Command> defaultCommands;

    @Inject
    public OperatorMainViewModelImpl(final StatisticsController statisticsController, final DataPlotter dataPlotter,
            final ExceptionHandler exceptionHandler, final OperatorDataFetcher dataFetcher,
            final OperatorContractManager contractManager) {
        super(statisticsController, dataPlotter, exceptionHandler, dataFetcher, contractManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected OperatorContractManager getContractManager() {
        return (OperatorContractManager) super.getContractManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected OperatorDataFetcher getDataFetcher() {
        return (OperatorDataFetcher) super.getDataFetcher();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeCommands() {
        super.initializeCommands();
        this.getCommands().add(new LabeledCommand("Search", args -> {
            this.jumpToSearchUser();
        }));
        this.defaultCommands = List.copyOf(this.getCommands());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeStatistics(final User user) {
        final List<Data> initialData = this.getDataFetcher().getGeneralData();
        this.getStatisticsController().setData(initialData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToSearchUser() {
        this.pushViewModel(SearchUserViewModel.class, searchUserController -> {
            searchUserController.setUserFoundEventHandler(evtArgs -> {
                if (evtArgs != null && evtArgs.getEventItem() != null) {
                    /**
                     * Pop the {@link SearchUserViewModel}
                     */
                    this.popViewModel();
                    final User user = evtArgs.getEventItem();
                    this.initializeStatisticsForSelectedUser(user, null);
                }
            });
            searchUserController.setContractFoundEventHandler(evtArgs -> {
                if (evtArgs != null && evtArgs.getEventItem() != null) {
                    /**
                     * Pop the {@link SearchUserViewModel}
                     */
                    this.popViewModel();
                    final User user = evtArgs.getEventItem().getKey();
                    this.initializeStatisticsForSelectedUser(user, List.of(evtArgs.getEventItem().getValue()));
                }
            });
        }, false);
    }

    /**
     * Initialize the statistics when a new user is selected.
     *
     * @param newUser           new user
     * @param selectedContracts selected contracts
     */
    private void initializeStatisticsForSelectedUser(final User newUser, final List<Contract> selectedContracts) {
        final List<Contract> allContracts = this.getContractManager().fetchContractsByUser(newUser);
        this.setUserContracts(allContracts);
        this.getSelectedContracts().clear();
        final List<Contract> contracts = selectedContracts == null ? allContracts : selectedContracts;
        this.getSelectedContracts().addAll(contracts);
        final List<Data> initialData = this.getDataFetcher().fetchAllUserData(newUser, contracts);
        this.getStatisticsController().setData(initialData);

        this.getCommands().clear();
        this.getCommands().addAll(this.defaultCommands);
        this.getCommands()
                .addAll(new LabeledCommand("Remove current selection", args -> this.removeSelectedUser()),
                        new LabeledCommand("See selected user profile",
                                args -> this.pushViewModel(UserProfileViewModel.class, userProfileController -> {
                                    userProfileController.setUserContracts(allContracts);
                                    userProfileController.setUser(newUser);
                                    userProfileController.setDeleteUserContractHandler(evtArgs -> {
                                        final Contract contractToDelete = evtArgs.getEventItem();
                                        if (this.getContractManager().deleteUserContract(contractToDelete)) {
                                            this.getContracts().remove(contractToDelete);
                                            this.removeSelectedContract(contractToDelete);
                                        }
                                    });
                                    userProfileController.setDeleteUserHandler(evtArgs -> {
                                        this.removeSelectedUser();
                                        this.popViewModel();
                                    });
                                }, false)),
                        new LabeledCommand("Add contract to the selected user", args -> {
                            this.pushViewModel(ContractCreationViewModel.class, contractCreationController -> {
                                contractCreationController.setUser(newUser);
                                contractCreationController.setContractCreateEventHandler(evtArgs -> {
                                    this.getContracts().add(evtArgs.getEventItem());
                                    this.popViewModel();
                                });
                            }, false);
                        }), new LabeledCommand("History", args -> this.pushViewModel(HistoryViewModel.class,
                                controller -> controller.setContracts(this.getSelectedContracts()), false)));

        this.setSelectedUser(newUser);
    }

    /**
     * Set the selected user.
     *
     * @param newUser user that needs to be marked as selected
     */
    private void setSelectedUser(final User newUser) {
        this.selectedUser().set(newUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return this.getSelectedUser()
                .map(elem -> super.getAvailableServiceTypes())
                .orElse(Arrays.stream(ServiceType.values()).collect(Collectors.toUnmodifiableSet()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getSelectedUser() {
        return Optional.ofNullable(this.selectedUser().get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectProperty<User> selectedUser() {
        return this.selectedUserProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSelectedUser() {
        this.initializeStatistics(this.getUser());
        this.getContracts().clear();
        this.getSelectedContracts().clear();
        this.selectedUser().set(null);
        this.getCommands().clear();
        this.getCommands().addAll(this.defaultCommands);
    }
}
