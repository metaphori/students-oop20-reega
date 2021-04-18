package reega.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.tuple.Pair;
import reega.data.ContractManager;
import reega.data.DataFetcher;
import reega.data.exporter.ExportFormat;
import reega.data.exporter.ReegaExporterFactory;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;
import reega.logging.ExceptionHandler;
import reega.statistics.DataPlotter;
import reega.statistics.StatisticsController;
import reega.users.User;
import reega.viewutils.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainControllerImpl extends AbstractController implements MainController {

    private User currentUser;
    private final StatisticsController statisticsController;
    private final DataPlotter dataPlotter;
    private final ExceptionHandler exceptionHandler;
    private List<Contract> contracts;
    private final ObservableList<Contract> selectedContracts = FXCollections.observableArrayList();
    private final ObservableList<Command> commands = FXCollections.observableArrayList();
    private EventHandler<Void> logoutEventHandler;
    private final DataFetcher dataFetcher;
    private final ContractManager contractManager;

    @Inject
    public MainControllerImpl(final StatisticsController statisticsController,
                              final DataPlotter dataPlotter,
                              final ExceptionHandler exceptionHandler,
                              final DataFetcher dataFetcher,
                              final ContractManager contractManager) {
        this.statisticsController = statisticsController;
        this.dataPlotter = dataPlotter;
        this.exceptionHandler = exceptionHandler;
        this.dataFetcher = dataFetcher;
        this.contractManager = contractManager;
    }

    protected void initializeCommands() {
        this.commands.add(new LabeledCommand("Export to CSV", args ->
                DialogFactory.getDefaultSaveDialog().openSaveDialog("CSV Files", ".csv").ifPresent(file ->
                        this.exportDataToFile(ExportFormat.CSV, file))));
        this.commands.add(new LabeledCommand("Export to JSON", args ->
                DialogFactory.getDefaultSaveDialog().openSaveDialog("JSON Files", ".json").ifPresent(file ->
                        this.exportDataToFile(ExportFormat.JSON, file))));
    }

    /**
     * Get the contract manager
     *
     * @return the contract manager
     */
    protected ContractManager getContractManager() {
        return this.contractManager;
    }

    /**
     * Get the data fetcher
     *
     * @return the data fetcher
     */
    protected DataFetcher getDataFetcher() {
        return this.dataFetcher;
    }

    /**
     * Get the statistics controller
     *
     * @return the statistics controller
     */
    protected StatisticsController getStatisticsController() {
        return this.statisticsController;
    }

    protected final void setUserContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * Initialize the statistics when a new user is set
     *
     * @param user user used for the statistics calculations
     */
    protected void initializeStatistics(final User user) {
        final List<Contract> allContracts = this.contractManager.fetchUserContracts();
        this.contracts = allContracts;
        this.selectedContracts.clear();
        this.selectedContracts.addAll(allContracts);
        List<Data> initialData = this.dataFetcher.fetchAllUserData(user, allContracts);
        this.getStatisticsController().setData(initialData);
    }

    private void exportDataToFile(ExportFormat format, File file) {
        try {
            ReegaExporterFactory.export(format, this.statisticsController.getCurrentData(), file.getAbsolutePath());
        } catch (IOException e) {
            this.exceptionHandler.handleException(e);
        }
    }

    @Override
    public ObservableList<Command> getCommands() {
        return this.commands;
    }

    @Override
    public void addSelectedContract(Contract contract) {
        List<Data> newData = this.dataFetcher.pushAndFetchContract(this.getStatisticsController().getCurrentData(), contract);
        this.getStatisticsController().setData(newData);
        this.selectedContracts.add(contract);
    }

    @Override
    public void removeSelectedContract(Contract contract) {
        final List<Data> newData = this.dataFetcher.removeAndFetchContract(this.getStatisticsController().getCurrentData(), contract);
        this.getStatisticsController().setData(newData);
        this.selectedContracts.remove(contract);
    }

    @Override
    public void setUser(final User newUser) {
        this.initializeStatistics(newUser);
        this.initializeCommands();
        this.currentUser = newUser;
    }

    @Override
    public User getUser() {
        return this.currentUser;
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
        return this.selectedContracts
                .stream()
                .flatMap(elem -> elem.getServices().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void setOnLogout(EventHandler<Void> evtHandler) {
        this.logoutEventHandler = evtHandler;
    }

    @Override
    public void logout() {
        this.logoutEventHandler.handle(null);
        this.pushController(LoginController.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataPlotter getDataPlotter() {
        return this.dataPlotter;
    }

}
