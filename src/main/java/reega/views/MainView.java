package reega.views;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import org.apache.commons.lang3.StringUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import reega.controllers.MainViewModel;
import reega.data.models.ServiceType;
import reega.statistics.DataPlotter;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewcomponents.MaxWidthButton;
import reega.viewcomponents.WrappableLabel;
import reega.viewutils.Command;
import reega.viewutils.ReegaFXMLLoader;
import reega.viewutils.ViewUtils;

public abstract class MainView extends GridPane {

    @FXML
    private WrappableLabel userEmail;
    @FXML
    private WrappableLabel managedUser;
    @FXML
    private FlexibleGridPane servicesPane;
    @FXML
    private VBox graphPane;
    @FXML
    private HBox contractsPane;
    @FXML
    private VBox buttonsPane;
    @FXML
    private ToggleButton logoutButton;

    public MainView(final MainViewModel viewModel) {
        ReegaFXMLLoader.loadFXML(this, "views/Main.fxml");

        this.userEmail.setText("Logged in as: " + viewModel.getUser().getFullName());
        this.populateButtonsPane(viewModel);


        this.graphPane.setVisible(false);
        this.graphPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.servicesPane.setVisible(false);
                return;
            }
            this.servicesPane.setVisible(true);
        });
        this.graphPane.managedProperty().bind(this.graphPane.visibleProperty());
        this.servicesPane.managedProperty().bind(this.servicesPane.visibleProperty());
        this.populateGraphPane();
        this.logoutButton.setOnAction(e -> viewModel.logout());
        viewModel.getCommands().addListener((ListChangeListener<? super Command>) change -> {
            this.populateButtonsPane(viewModel);
        });
        this.managedUser.managedProperty().bind(this.managedUser.visibleProperty());
    }

    protected final WrappableLabel getManagedUser() {
        return this.managedUser;
    }

    /**
     * Get the services pane
     *
     * @return the services pane
     */
    protected final FlexibleGridPane getServicesPane() {
        return this.servicesPane;
    }

    /**
     * Get the contracts pane
     *
     * @return the contracts pane
     */
    protected final HBox getContractsPane() {
        return this.contractsPane;
    }

    /**
     * Get the graphPane pane
     *
     * @return the graph pane
     */
    protected final VBox getGraphPane() {
        return this.graphPane;
    }

    /**
     * Populate the {@link #buttonsPane}
     *
     * @param viewModel viewModel used to populate the {@link #buttonsPane}
     */
    private void populateButtonsPane(final MainViewModel viewModel) {
        this.buttonsPane.getChildren().clear();
        this.buttonsPane.getChildren().addAll(viewModel.getCommands().stream().map(entry -> {
            final MaxWidthButton b = new MaxWidthButton(entry.getCommandName());
            b.setOnAction(event -> {
                entry.execute((Object) null);
            });
            return b;
        }).collect(Collectors.toList()));
    }

    /**
     * Populate the {@link #servicesPane}
     *
     * @param viewModel viewModel used to populate the {@link #servicesPane}
     */
    protected final void populateServicesPane(final MainViewModel viewModel) {
        this.getServicesPane().getChildren().clear();
        viewModel.getAvailableServiceTypes().forEach(svcType -> {
            String svcTypeMeasurementUnit = ServiceType.getMeasurementUnit(svcType);
            final Card serviceCard = ViewUtils.wrapNodeWithStyleClasses(new Card(), "svc-card");
            // Set the service card mouse clicked event
            serviceCard.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    viewModel.getDataPlotter().setServiceType(svcType);
                    this.updateAndShowGraph(viewModel.getDataPlotter());
                }
            });
            final ObservableList<Node> serviceCardChildren = serviceCard.getChildren();
            // Add the header of the card
            serviceCardChildren.add(ViewUtils
                    .wrapNodeWithStyleClasses(new WrappableLabel(StringUtils.capitalize(svcType.getName())), "svc-header"));
            // Add the peek if it's present
            viewModel.getPeek(svcType).ifPresent(peek -> {
                DateFormat usDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(
                        new WrappableLabel("Peek date: " + usDateFormat.format(peek.getKey())), "svc-peek"));
                serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(
                        new WrappableLabel(String.format(Locale.US, "Peek value: %.2f %s", peek.getValue(), svcTypeMeasurementUnit)), "svc-peek"));
            });
            // Add the average usage
            serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(
                    new WrappableLabel(String.format(Locale.US, "Average usage: %.2f %s", viewModel.getAverageUsage(svcType), svcTypeMeasurementUnit)),
                    "svc-avg"));
            // Add the total usage
            serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(
                    new WrappableLabel(String.format(Locale.US, "Total usage: %.2f %s", viewModel.getTotalUsage(svcType), svcTypeMeasurementUnit)),
                    "svc-tot"));
            this.getServicesPane().getChildren().add(serviceCard);
        });
    }

    /**
     * Populate the {@link #contractsPane}
     *
     * @param viewModel viewModel used to populate the {@link #contractsPane}
     */
    protected final void populateContractsPane(final MainViewModel viewModel) {
        this.getContractsPane().getChildren().clear();
        this.getContractsPane().getChildren().addAll(viewModel.getContracts().stream().map(contract -> {
            final CheckBox checkBox = new CheckBox();
            checkBox.setText(contract.getAddress());
            // If the selectedContracts contain the element then set the selected property to true
            final boolean contractIsSelected = viewModel.getSelectedContracts().contains(contract);
            checkBox.selectedProperty().set(contractIsSelected);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                // Add the contract if the selectedProperty is true
                if (newValue) {
                    viewModel.addSelectedContract(contract);
                }
                // Remove the contract if the selectedProperty is false
                else {
                    viewModel.removeSelectedContract(contract);
                }
                this.populateServicesPane(viewModel);
                if (this.graphPane.isVisible()) {
                    this.updateGraph(viewModel.getDataPlotter());
                }
            });
            return checkBox;
        }).collect(Collectors.toList()));
        this.getContractsPane().visibleProperty().set(this.getContractsPane().getChildren().size() >= 1);
    }

    /**
     * Populate the {@link #graphPane}
     */
    protected final void populateGraphPane() {
        this.getGraphPane().getChildren().clear();
        // prepare axis
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day of the month");
        xAxis.setTickLabelFormatter(ViewUtils.getDateStringConverter());
        NumberAxis yAxis = new NumberAxis();
        // auto ranging is true by default
        yAxis.setForceZeroInRange(false);
        xAxis.setForceZeroInRange(false);
        // prepare chart
        AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setMinSize(500.0, 300.0);
        this.graphPane.getChildren().add(chart);
        // prepare button
        Button button = new Button();
        button.setText("Back to usage");
        button.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                this.graphPane.setVisible(false);
            }
        });
        // prepare box
        HBox innerbox = ViewUtils.wrapNodeWithStyleClasses(new HBox(button), "back-button");
        innerbox.setAlignment(Pos.BOTTOM_RIGHT);
        this.graphPane.getChildren().add(innerbox);
        VBox.setVgrow(this.graphPane, Priority.ALWAYS);
    }

    /**
     * Updates data to be shown and sets the graphPane visible
     *
     * @param dataPlotter
     */
    protected void updateAndShowGraph(DataPlotter dataPlotter) {
        this.updateGraph(dataPlotter);
        this.graphPane.setVisible(true);
    }

    /**
     * Updates graph based on the given data type and dataPlotter
     *
     * @param dataPlotter
     */
    protected void updateGraph(DataPlotter dataPlotter) {
        AreaChart<Number, Number> chart = (AreaChart<Number, Number>) this.graphPane.getChildren().get(0);
        // set chart title
        chart.setTitle(StringUtils.capitalize(dataPlotter.getServiceType().getName()));
        // set
        chart.getYAxis().setLabel("Usage in " + ServiceType.getMeasurementUnit(dataPlotter.getServiceType()));
        // remove, create and add data to the chart
        chart.getData().clear();
        Series<Number, Number> dataSeries = new Series<>();
        dataSeries.getData()
                .addAll(dataPlotter.getData()
                        .entrySet()
                        .stream()
                        .map(elem -> new XYChart.Data<Number, Number>(elem.getKey(), elem.getValue()))
                        .collect(Collectors.toList()));
        chart.getData().add(dataSeries);
        chart.layout();
    }

}
