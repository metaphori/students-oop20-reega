package reega.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import javafx.beans.property.ObjectProperty;
import reega.data.ContractFetcher;
import reega.data.DataController;
import reega.data.DataFetcher;
import reega.data.OperatorDataFetcher;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import javafx.beans.property.SimpleObjectProperty;
import reega.statistics.DataPlotter;
import reega.statistics.StatisticsController;
import reega.users.User;
import reega.viewutils.LabeledCommand;

public class OperatorMainControllerImpl extends MainControllerImpl implements OperatorMainController {
    private ObjectProperty<User> selectedUserProperty = new SimpleObjectProperty<>();

    @Inject
    public OperatorMainControllerImpl(final StatisticsController statisticsController,
                                      final DataPlotter dataPlotter,
                                      final ExceptionHandler exceptionHandler,
                                      final OperatorDataFetcher dataFetcher,
                                      final ContractFetcher contractFetcher) {
        super(statisticsController, dataPlotter, exceptionHandler, dataFetcher, contractFetcher);
    }

    @Override
    protected OperatorDataFetcher getDataFetcher() {
        return (OperatorDataFetcher)super.getDataFetcher();
    }

    @Override
    protected void initializeCommands() {
        super.initializeCommands();
        this.getCommands().add(new LabeledCommand("Search user", (args) -> {
           this.jumpToSearchUser();
        }));
        this.getCommands().add(new LabeledCommand("Manage users", (args) -> {
           //TODO Create the ManagerUsers controller
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
                    this.setSelectedUser(evtArgs.getEventItem());
                }
            });
            searchUserController.setContractFoundEventHandler(evtArgs -> {
                if (evtArgs != null && evtArgs.getEventItem() != null) {
                    /**
                     * Pop the {@link SearchUserController}
                     */
                    this.popController();
                    // Search the user by the contractID
                    // Set the user as selected
                    this.getSelectedContracts().clear();
                    this.getSelectedContracts().add(evtArgs.getEventItem());
                }
            });
        }, false);
    }

    @Override
    public void setSelectedUser(final User newUser) {
        super.initializeStatistics(newUser);
        this.selectedUser().set(newUser);
        this.getCommands().add(new LabeledCommand("Remove current selection", args -> {
            this.removeSelectedUser();
        }));
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
        this.getCommands().remove(this.getCommands().size() - 1);
    }
}
