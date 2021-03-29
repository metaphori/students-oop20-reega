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

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperatorMainControllerImpl extends MainControllerImpl implements OperatorMainController{

    private ObjectProperty<User> selectedUserProperty;
    private ObservableList<Contract> selectedContracts = FXCollections.emptyObservableList();
    private List<Contract> contracts;
    private Map<Contract,List<Data>> currentDataByContract;


    public OperatorMainControllerImpl(StatisticsController statisticsController, DataController dataController, ExceptionHandler exceptionHandler) {
        super(statisticsController, dataController, exceptionHandler);
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
        List<Contract> contracts;
        try {
            //TODO Get user contracts by id
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

    private List<Data> getDataByContract(Contract contract) {
        try {
            return this.getDataController().getMonthlyData(contract.getId());
        } catch (IOException e) {
            this.getExceptionHandler().handleException(e, "Failed to load data for the contract: " + contract.getId());
        }
        return Collections.emptyList();
    }

    @Override
    public ObservableList<Contract> getSelectedContracts() {
        return selectedContracts;
    }

    @Override
    public void removeSelectedUser() {
        initializeStatistics();
        this.contracts = null;
        this.selectedContracts.clear();
        this.currentDataByContract.clear();
        this.selectedUser().set(null);
    }

    @Override
    public List<Contract> getContracts() {
        return this.contracts;
    }
}
