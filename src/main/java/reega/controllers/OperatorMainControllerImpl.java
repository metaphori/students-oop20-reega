package reega.controllers;

import javafx.collections.ObservableList;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.StatisticsController;
import reega.users.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OperatorMainControllerImpl extends AbstractMainController implements OperatorMainController{

    private Optional<User> selectedUser;
    private Optional<ObservableList<Contract>> selectedContracts;


    public OperatorMainControllerImpl(StatisticsController statisticsController, DataController dataController, ExceptionHandler exceptionHandler) {
        super(statisticsController, dataController, exceptionHandler);
    }

    @Override
    protected void initializeStatistics() {
        try {
            List<Data> generalMonthlyData = this.getDataController().getMonthlyData(null);
            this.getStatisticsController().setData(generalMonthlyData);
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e, "Failed to load general data");
            return;
        }
    }

    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return Arrays.stream(ServiceType.values()).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<User> getSelectedUser() {
        return selectedUser;
    }

    @Override
    public Optional<ObservableList<Contract>> getSelectedContracts() {
        return selectedContracts;
    }
}
