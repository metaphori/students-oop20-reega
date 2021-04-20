package reega.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import reega.viewutils.AbstractViewModel;
import reega.viewutils.Command;
import reega.viewutils.DialogFactory;
import reega.viewutils.EventHandler;
import reega.viewutils.LabeledCommand;

public class MainViewModelImpl extends AbstractViewModel implements MainViewModel {

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
    public MainViewModelImpl(final StatisticsController statisticsController, final DataPlotter dataPlotter,
            final ExceptionHandler exceptionHandler, final DataFetcher dataFetcher,
            final ContractManager contractManager) {
        this.statisticsController = statisticsController;
        this.dataPlotter = dataPlotter;
        this.exceptionHandler = exceptionHandler;
        this.dataFetcher = dataFetcher;
        this.contractManager = contractManager;
    }

    /**
     * Initialize the commands.
     */
    protected void initializeCommands() {
        this.commands.add(new LabeledCommand("Export to CSV",
                args -> DialogFactory.getDefaultSaveDialog()
                        .openSaveDialog("CSV Files", ".csv")
                        .ifPresent(file -> this.exportDataToFile(ExportFormat.CSV, file))));
        this.commands.add(new LabeledCommand("Export to JSON",
                args -> DialogFactory.getDefaultSaveDialog()
                        .openSaveDialog("JSON Files", ".json")
                        .ifPresent(file -> this.exportDataToFile(ExportFormat.JSON, file))));
    }

    /**
     * Get the contract manager.
     *
     * @return the contract manager
     */
    protected ContractManager getContractManager() {
        return this.contractManager;
    }

    /**
     * Get the data fetcher.
     *
     * @return the data fetcher
     */
    protected DataFetcher getDataFetcher() {
        return this.dataFetcher;
    }

    /**
     * Get the statistics controller.
     *
     * @return the statistics controller
     */
    protected StatisticsController getStatisticsController() {
        return this.statisticsController;
    }

    protected final void setUserContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * Initialize the statistics when a new user is set.
     *
     * @param user user used for the statistics calculations
     */
    protected void initializeStatistics(final User user) {
        final List<Contract> allContracts = this.contractManager.fetchUserContracts();
        this.contracts = allContracts;
        this.selectedContracts.clear();
        this.selectedContracts.addAll(allContracts);
        final List<Data> initialData = this.dataFetcher.fetchAllUserData(user, allContracts);
        this.getStatisticsController().setData(initialData);
    }

    private void exportDataToFile(final ExportFormat format, final File file) {
        try {
            ReegaExporterFactory.export(format, this.statisticsController.getCurrentData(), file.getAbsolutePath());
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableList<Command> getCommands() {
        return this.commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSelectedContract(final Contract contract) {
        final List<Data> newData = this.dataFetcher
                .pushAndFetchContract(this.getStatisticsController().getCurrentData(), contract);
        this.getStatisticsController().setData(newData);
        this.selectedContracts.add(contract);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSelectedContract(final Contract contract) {
        final List<Data> newData = this.dataFetcher
                .removeAndFetchContract(this.getStatisticsController().getCurrentData(), contract);
        this.getStatisticsController().setData(newData);
        this.selectedContracts.remove(contract);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUser(final User newUser) {
        this.initializeStatistics(newUser);
        this.initializeCommands();
        this.currentUser = newUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser() {
        return this.currentUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Pair<Date, Double>> getPeek(final ServiceType svcType) {
        return this.statisticsController.getPeek(svcType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAverageUsage(final ServiceType svcType) {
        return this.statisticsController.getAverageUsage(svcType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTotalUsage(final ServiceType svcType) {
        return this.statisticsController.getTotalUsage(svcType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableList<Contract> getSelectedContracts() {
        return this.selectedContracts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contract> getContracts() {
        return this.contracts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ServiceType> getAvailableServiceTypes() {
        return this.selectedContracts.stream()
                .flatMap(elem -> elem.getServices().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnLogout(final EventHandler<Void> evtHandler) {
        this.logoutEventHandler = evtHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout() {
        this.logoutEventHandler.handle(null);
        this.pushViewModel(LoginViewModel.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataPlotter getDataPlotter() {
        return this.dataPlotter;
    }

}
