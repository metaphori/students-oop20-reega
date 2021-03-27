package reega.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.StatisticsController;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMainControllerImpl extends AbstractMainController implements UserMainController{

    private List<Contract> contracts;
    private ObservableList<Contract> selectedContracts = FXCollections.observableArrayList();
    private Map<Contract,List<Data>> currentDataByContract;

    @Inject
    public UserMainControllerImpl(StatisticsController statisticsController, DataController dataController, ExceptionHandler exceptionHandler) {
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
        return this.selectedContracts;
    }

    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return this.currentDataByContract.keySet().stream().flatMap(elem -> elem.getServices().stream()).collect(Collectors.toUnmodifiableSet());
    }
}
