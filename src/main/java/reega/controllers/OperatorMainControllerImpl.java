package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.StatisticsController;
import reega.users.User;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperatorMainControllerImpl extends MainControllerImpl implements OperatorMainController{

    private ObjectProperty<User> selectedUserProperty;

    @Inject
    public OperatorMainControllerImpl(StatisticsController statisticsController, DataController dataController, ExceptionHandler exceptionHandler) {
        super(statisticsController, dataController, exceptionHandler);
    }

    @Override
    protected void initializeStatistics(User user) {
        try {
            List<Data> generalMonthlyData = this.getDataController().getMonthlyData(null);
            this.getStatisticsController().setData(generalMonthlyData);
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e, "Failed to load general data");
            return;
        }
    }

    @Override
    public void jumpToSearchUser() {
        this.pushController(SearchUserController.class, searchUserController -> {
            searchUserController.setUserFoundEventHandler(evtArgs -> {
                if (evtArgs != null && evtArgs.getEventItem() != null) {
                    this.setSelectedUser(evtArgs.getEventItem());
                }
            });
        }, false);
    }

    @Override
    public void setSelectedUser(User newUser) {
        this.fetchAndLoadUserData(newUser);
        this.selectedUser().set(newUser);
    }

    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return getSelectedUser().map(elem -> {
            List<Contract> contracts;
            try {
                //TODO Get user contracts by id
                contracts = this.getDataController().getUserContracts();
            } catch (IOException | SQLException e) {
                this.getExceptionHandler().handleException(e, "Failed to load contracts for the user");
                return null;
            }
            return contracts.stream().flatMap(contract -> contract.getServices().stream()).collect(Collectors.toUnmodifiableSet());
        }).orElse(Arrays.stream(ServiceType.values()).collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public Optional<User> getSelectedUser() {
        return Optional.ofNullable(selectedUser().get());
    }

    @Override
    public ObjectProperty<User> selectedUser() {
        return this.selectedUserProperty;
    }

    @Override
    public void removeSelectedUser() {
        initializeStatistics(this.getUser());
        this.getContracts().clear();
        this.getSelectedContracts().clear();
        this.getCurrentDataByContract().clear();
        this.selectedUser().set(null);
    }
}
