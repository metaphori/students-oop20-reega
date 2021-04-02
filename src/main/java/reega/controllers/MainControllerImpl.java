package reega.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import reega.viewutils.AbstractController;
import reega.viewutils.Command;

public class MainControllerImpl extends AbstractController implements MainController {

    final ObjectProperty<User> user = new SimpleObjectProperty<>();
    private final StatisticsController statisticsController;
    private final DataController dataController;
    private final ExceptionHandler exceptionHandler;
    private List<Contract> contracts;
    private final ObservableList<Contract> selectedContracts = FXCollections.observableArrayList();
    private Map<Contract, List<Data>> currentDataByContract;
    private Map<String, Command> commands;

    @Inject
    public MainControllerImpl(final StatisticsController statisticsController, final DataController dataController,
            final ExceptionHandler exceptionHandler) {
        this.statisticsController = statisticsController;
        this.dataController = dataController;
        this.exceptionHandler = exceptionHandler;
        this.selectedContracts.addListener((ListChangeListener<Contract>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    final Stream<Data> newDataStream = c.getAddedSubList().stream().flatMap(contract -> {
                        final List<Data> monthlyData = this.getDataByContract(contract);
                        this.currentDataByContract.put(contract, monthlyData);
                        return monthlyData.stream();
                    });
                    final List<Data> allData = Stream
                            .concat(newDataStream, this.currentDataByContract.values().stream().flatMap(Collection::stream))
                            .collect(Collectors.toList());
                    this.getStatisticsController().setData(allData);
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(contract -> this.currentDataByContract.remove(contract));
                    this.getStatisticsController()
                            .setData(this.currentDataByContract.values()
                                    .stream()
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toList()));
                }
            }
        });
    }

    protected void initializeCommands() {
        this.commands = new TreeMap<>();
        this.commands.put("Export to CSV", args -> {
            // TODO Make an exporter
        });
    }

    @Override
    public Map<String, Command> getCommands() {
        return this.commands;
    }

    /**
     * Get the statistics controller
     *
     * @return the statistics controller
     */
    protected StatisticsController getStatisticsController() {
        return this.statisticsController;
    }

    /**
     * Get the data controller
     *
     * @return the data controller
     */
    protected DataController getDataController() {
        return this.dataController;
    }

    /**
     * Get the exception handler
     *
     * @return the exception handler
     */
    protected ExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    /**
     * Get the current data by contract
     *
     * @return the current data by contract
     */
    protected Map<Contract, List<Data>> getCurrentDataByContract() {
        return this.currentDataByContract;
    }

    /**
     * Initialize the statistics when a new user is set
     *
     * @param user user used for the statistics calculations
     */
    protected void initializeStatistics(final User user) {
        this.fetchAndLoadUserData(user);
    }

    /**
     * Fetch the user data (contracts and data) and load it into the fields
     *
     * @param user user used to load data
     */
    protected final void fetchAndLoadUserData(final User user) {
        List<Contract> contracts;
        try {
            contracts = this.getDataController().getUserContracts();
        } catch (IOException | SQLException e) {
            this.getExceptionHandler().handleException(e, "Failed to load contracts for the user");
            return;
        }
        this.contracts = contracts;
        this.currentDataByContract = new HashMap<>();
        this.selectedContracts.clear();
        this.selectedContracts.addAll(contracts);
        final List<Data> data = contracts.stream().flatMap(contract -> {
            final List<Data> monthlyData = this.getDataByContract(contract);
            this.currentDataByContract.put(contract, monthlyData);
            return monthlyData.stream();
        }).collect(Collectors.toList());
        this.getStatisticsController().setData(data);
    }

    @Override
    public void setUser(final User newUser) {
        this.initializeStatistics(newUser);
        this.initializeCommands();
        this.user().set(newUser);
    }

    @Override
    public User getUser() {
        return this.user().get();
    }

    @Override
    public ObjectProperty<User> user() {
        return this.user;
    }

    @Override
    public Optional<Pair<Date, Double>> getPeek(final ServiceType svcType) {
        return this.statisticsController.getPeek(svcType);
    }

    @Override
    public double getAverageUsage(final ServiceType svcType) {
        return this.statisticsController.getAverageUsage(svcType);
    }

    @Override
    public double getTotalUsage(final ServiceType svcType) {
        return this.statisticsController.getTotalUsage(svcType);
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
        return this.currentDataByContract.keySet()
                .stream()
                .flatMap(elem -> elem.getServices().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Get the data by a contract
     *
     * @param contract contract to search
     * @return a list of data containing data for the contract
     */
    protected final List<Data> getDataByContract(final Contract contract) {
        try {
            return this.getDataController().getMonthlyData(contract.getId());
        } catch (final IOException e) {
            this.getExceptionHandler().handleException(e, "Failed to load data for the contract: " + contract.getId());
        }
        return Collections.emptyList();
    }
}
