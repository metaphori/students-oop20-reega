package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.tuple.Pair;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.StatisticsController;
import reega.users.User;
import reega.viewutils.AbstractController;
import reega.viewutils.Controller;
import reega.viewutils.ControllerChangedEventHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainControllerImpl extends AbstractController implements MainController {

    final ObjectProperty<User> user = new SimpleObjectProperty<>();
    private final StatisticsController statisticsController;
    private final DataController dataController;
    private final ExceptionHandler exceptionHandler;
    private List<Contract> contracts;
    private ObservableList<Contract> selectedContracts = FXCollections.observableArrayList();
    private Map<Contract,List<Data>> currentDataByContract;

    @Inject
    public MainControllerImpl(StatisticsController statisticsController, DataController dataController, ExceptionHandler exceptionHandler) {
        this.statisticsController = statisticsController;
        this.dataController = dataController;
        this.exceptionHandler = exceptionHandler;
        this.selectedContracts.addListener((ListChangeListener<Contract>) c -> {
            if (c.wasAdded()) {
                Stream<Data> newDataStream = c.getAddedSubList().stream().flatMap(contract -> {
                    List<Data> monthlyData = this.getDataByContract(contract);
                    this.currentDataByContract.put(contract,monthlyData);
                    return monthlyData.stream();
                });
                List<Data> allData = Stream.concat(newDataStream, this.currentDataByContract.values().stream().flatMap(Collection::stream)).collect(Collectors.toList());
                this.getStatisticsController().setData(allData);
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(contract -> this.currentDataByContract.remove(contract));
                this.getStatisticsController().setData(currentDataByContract.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
            }
        });
    }

    protected StatisticsController getStatisticsController() {
        return this.statisticsController;
    }

    protected DataController getDataController() {
        return this.dataController;
    }

    protected ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    protected void initializeStatistics(User user) {
        this.fetchUserData(user);
    }

    protected void fetchUserData(User user) {
        List<Contract> contracts;
        try {
            contracts = this.getDataController().getUserContracts();
        } catch (IOException | SQLException e) {
            this.getExceptionHandler().handleException(e, "Failed to load contracts for the user");
            return;
        }
        this.contracts = contracts;
        this.selectedContracts.clear();
        this.selectedContracts.addAll(contracts);
        this.currentDataByContract = new HashMap<>();
        List<Data> data = contracts.stream().flatMap(contract -> {
            List<Data> monthlyData = this.getDataByContract(contract);
            this.currentDataByContract.put(contract,monthlyData);
            return monthlyData.stream();
        }).collect(Collectors.toList());
        this.getStatisticsController().setData(data);
    }

    @Override
    public void setUser(User user) {
        initializeStatistics(user);
        this.user().set(user);
    }

    @Override
    public User getUser() {
        return this.user().get();
    }

    @Override
    public ObjectProperty<User> user() {
        return this.user();
    }

    @Override
    public Optional<Pair<Date, Double>> getPeek(ServiceType svcType) {
        return this.statisticsController.getPeek(svcType);
    }

    @Override
    public double getAverageUsage(ServiceType svcType) {
        return this.statisticsController.getAverageUsage(svcType);
    }

    @Override
    public double getTotalUsage(ServiceType svcType) {
        return this.statisticsController.getTotalUsage(svcType);
    }

    protected final List<Data> getDataByContract(Contract contract) {
        try {
            return this.getDataController().getMonthlyData(contract.getId());
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e, "Failed to load data for the contract: " + contract.getId());
        }
        return Collections.emptyList();
    }

    @Override
    public ObservableList<Contract> getSelectedContracts() {
        return this.selectedContracts;
    }

    @Override
    public List<Contract> getContracts() {
        return this.contracts;
    }

    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return this.currentDataByContract.keySet().stream().flatMap(elem -> elem.getServices().stream()).collect(Collectors.toUnmodifiableSet());
    }
}
