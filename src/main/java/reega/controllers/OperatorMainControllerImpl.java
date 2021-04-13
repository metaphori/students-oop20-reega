package reega.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.data.ContractFetcher;
import reega.data.OperatorContractFetcher;
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

public class OperatorMainControllerImpl extends MainControllerImpl implements OperatorMainController {
    private ObjectProperty<User> selectedUserProperty = new SimpleObjectProperty<>();

    @Inject
    public OperatorMainControllerImpl(final StatisticsController statisticsController, final DataPlotter dataPlotter,
            final ExceptionHandler exceptionHandler, final OperatorDataFetcher dataFetcher,
            final OperatorContractFetcher contractFetcher) {
        super(statisticsController, dataPlotter, exceptionHandler, dataFetcher, contractFetcher);
    }

    @Override
    protected OperatorContractFetcher getContractFetcher() {
        return (OperatorContractFetcher) super.getContractFetcher();
    }

    @Override
    protected OperatorDataFetcher getDataFetcher() {
        return (OperatorDataFetcher) super.getDataFetcher();
    }

    @Override
    protected void initializeCommands() {
        super.initializeCommands();
        this.getCommands().add(new LabeledCommand("Search user", (args) -> {
            this.jumpToSearchUser();
        }));
        this.getCommands().add(new LabeledCommand("Manage users", (args) -> {
            // TODO Create the ManagerUsers controller
        }));
    }

    @Override
    protected void initializeStatistics(final User user) {
        List<Data> initialData = this.getDataFetcher().getGeneralData();
        this.getStatisticsController().setData(initialData);
    }

    @Override
    public void jumpToSearchUser() {
        this.pushController(SearchUserController.class, searchUserController -> {
            searchUserController.setUserFoundEventHandler(evtArgs -> {
                if (evtArgs != null && evtArgs.getEventItem() != null) {
                    /**
                     * Pop the {@link SearchUserController}
                     */
                    this.popController();
                    User user = evtArgs.getEventItem();
                    this.initializeStatisticsForSelectedUser(user, null);
                }
            });
            searchUserController.setContractFoundEventHandler(evtArgs -> {
                if (evtArgs != null && evtArgs.getEventItem() != null) {
                    /**
                     * Pop the {@link SearchUserController}
                     */
                    this.popController();
                    User user = evtArgs.getEventItem().getKey();
                    this.initializeStatisticsForSelectedUser(user, List.of(evtArgs.getEventItem().getValue()));
                }
            });
        }, false);
    }

    /**
     * Initialize the statistics when a new user is selected
     * @param newUser new user
     */
    private void initializeStatisticsForSelectedUser(final User newUser, final List<Contract> selectedContracts) {
        final List<Contract> allContracts = this.getContractFetcher().fetchContractsByUser(newUser);
        this.setUserContracts(allContracts);
        this.getSelectedContracts().clear();
        List<Contract> contracts = selectedContracts == null ? allContracts : selectedContracts;
        this.getSelectedContracts().addAll(contracts);
        List<Data> initialData = this.getDataFetcher().fetchAllUserData(newUser, contracts);
        this.getStatisticsController().setData(initialData);
        if (this.selectedUser().isNull().get()) {
            this.getCommands().add(new LabeledCommand("Remove current selection", args -> {
                this.removeSelectedUser();
            }));
        }
        else {
            this.getCommands().remove(this.getCommands().size() - 1);
        }

        this.getCommands().add(new LabeledCommand("See selected user profile", args -> {
            this.pushController(UserProfileController.class, userProfileController -> {
                userProfileController.setUserContracts(allContracts);
                userProfileController.setUser(newUser);
            }, false);
        }));
        this.setSelectedUser(newUser);
    }

    /**
     * Set the selected user
     * @param newUser user that needs to be marked as selected
     */
    private void setSelectedUser(final User newUser) {
        this.selectedUser().set(newUser);
    }

    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return this.getSelectedUser()
                .map(elem -> super.getAvailableServiceTypes())
                .orElse(Arrays.stream(ServiceType.values()).collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public Optional<User> getSelectedUser() {
        return Optional.ofNullable(this.selectedUser().get());
    }

    @Override
    public ObjectProperty<User> selectedUser() {
        return this.selectedUserProperty;
    }

    @Override
    public void removeSelectedUser() {
        this.initializeStatistics(this.getUser());
        this.getContracts().clear();
        this.getSelectedContracts().clear();
        this.selectedUser().set(null);
        this.getCommands().remove(this.getCommands().size() - 2, this.getCommands().size());
    }
}
