package reega.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import javafx.beans.property.ObjectProperty;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.StatisticsController;
import reega.users.User;

public class OperatorMainControllerImpl extends MainControllerImpl implements OperatorMainController {

    private ObjectProperty<User> selectedUserProperty;

    @Inject
    public OperatorMainControllerImpl(final StatisticsController statisticsController,
            final DataController dataController, final ExceptionHandler exceptionHandler) {
        super(statisticsController, dataController, exceptionHandler);
    }

    @Override
    protected void initializeCommands() {
        super.initializeCommands();
        this.getCommands().put("Search user", (args) -> {
           this.jumpToSearchUser();
        });
    }

    @Override
    protected void initializeStatistics(final User user) {
        try {
            final List<Data> generalMonthlyData = this.getDataController().getMonthlyData(null);
            this.getStatisticsController().setData(generalMonthlyData);
        } catch (final IOException e) {
            this.getExceptionHandler().handleException(e, "Failed to load general data");
            return;
        }
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
        }, false);
    }

    @Override
    public void setSelectedUser(final User newUser) {
        this.fetchAndLoadUserData(newUser);
        this.selectedUser().set(newUser);
    }

    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return this.getSelectedUser().map(elem -> {
            List<Contract> contracts;
            try {
                // TODO Get user contracts by id
                contracts = this.getDataController().getUserContracts();
            } catch (IOException | SQLException e) {
                this.getExceptionHandler().handleException(e, "Failed to load contracts for the user");
                return null;
            }
            return contracts.stream()
                    .flatMap(contract -> contract.getServices().stream())
                    .collect(Collectors.toUnmodifiableSet());
        }).orElse(Arrays.stream(ServiceType.values()).collect(Collectors.toUnmodifiableSet()));
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
        this.getCurrentDataByContract().clear();
        this.selectedUser().set(null);
    }
}
