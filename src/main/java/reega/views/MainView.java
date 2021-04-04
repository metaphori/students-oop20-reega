package reega.views;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;
import reega.controllers.MainController;
import reega.data.models.Contract;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewutils.ViewUtils;

public abstract class MainView extends GridPane {

    @FXML
    private Label userEmail;
    @FXML
    private FlexibleGridPane servicesPane;
    @FXML
    private VBox graphPane;
    @FXML
    private HBox contractsPane;
    @FXML
    private VBox buttonsPane;

    public MainView(final MainController controller) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("views/Main.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        if (controller.user().isNotNull().get()) {
            this.userEmail.setText("Logged in as: " + controller.user().get().getFullName());
            this.populateButtonsPane(controller);
        }
        controller.user().addListener((observable, oldValue, newValue) -> {
            this.userEmail.setText("Logged in as: " + newValue.getFullName());
            this.populateButtonsPane(controller);
        });
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
    }

    private void populateButtonsPane(final MainController controller) {
        this.buttonsPane.getChildren().addAll(controller.getCommands().entrySet().stream().map(entry -> {
            final Button b = new Button(entry.getKey());
            b.setMaxWidth(1.7976931348623157E308);
            b.setOnAction(event -> {
                entry.getValue().execute((Object) null);
            });
            return b;
        }).collect(Collectors.toList()));
    }

    protected final FlexibleGridPane getServicesPane() {
        return this.servicesPane;
    }

    protected final HBox getContractsPane() {
        return this.contractsPane;
    }

    protected final void populateServicesPane(final MainController controller) {
        this.getServicesPane().getChildren().clear();
        controller.getAvailableServiceTypes().forEach(svcType -> {
            final Card serviceCard = new Card();
            serviceCard.getStyleClass().add("svc-card");
            final ObservableList<Node> serviceCardChildren = serviceCard.getChildren();
            serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(new Text(StringUtils.capitalize(svcType.getName())),"svc-header"));
            controller.getPeek(svcType).ifPresent(peek -> {
                DateFormat usDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(new Text("Peek date: " + usDateFormat.format(peek.getKey())), "svc-peek"));
                serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(new Text(String.format(Locale.US,"Peek value: %.2f", peek.getValue())), "svc-peek"));
            });
            serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(new Text(String.format(Locale.US,"Average usage: %.2f",controller.getAverageUsage(svcType))), "svc-avg"));
            serviceCardChildren.add(ViewUtils.wrapNodeWithStyleClasses(new Text(String.format(Locale.US,"Total usage: %.2f",controller.getTotalUsage(svcType))),"svc-tot"));
            this.getServicesPane().getChildren().add(serviceCard);
        });
    }

    protected final void populateContractsPane(final MainController controller) {
        this.getContractsPane().getChildren().clear();
        this.getContractsPane().getChildren().addAll(controller.getContracts().stream().map(elem -> {
            final CheckBox checkBox = new CheckBox();
            checkBox.setUserData(elem);
            checkBox.setText(elem.getAddress());
            if (controller.getSelectedContracts().indexOf(elem) != -1) {
                checkBox.selectedProperty().set(true);
            }
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    //controller.getSelectedContracts().add((Contract) checkBox.getUserData());
                    controller.addSelectedContract((Contract) checkBox.getUserData());
                }
                else {
                    controller.removeSelectedContract((Contract)checkBox.getUserData());
                }
                this.populateServicesPane(controller);
            });
            return checkBox;
        }).collect(Collectors.toList()));
        this.getContractsPane().visibleProperty().set(this.getContractsPane().getChildren().size() >= 1);
    }
}
