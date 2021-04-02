package reega.views;

import java.io.IOException;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
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
import reega.controllers.MainController;
import reega.data.models.Contract;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;

public abstract class MainView extends GridPane {

    @FXML
    private Label userEmail;
    @FXML
    private FlexibleGridPane servicesPane;
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
            this.userEmail.setText(controller.user().get().getEmail());
            this.populateButtonsPane(controller);
        }
        controller.user().addListener((observable, oldValue, newValue) -> {
            this.userEmail.setText(newValue.getEmail());
            this.populateButtonsPane(controller);
        });
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
            final ObservableList<Node> serviceCardChildren = serviceCard.getChildren();
            serviceCardChildren.add(new Text(svcType.getName()));
            controller.getPeek(svcType).ifPresent(peek -> {
                serviceCardChildren.add(new Text("Peek date: " + peek.getKey()));
                serviceCardChildren.add(new Text("Peek value: " + peek.getValue()));
            });
            serviceCardChildren.add(new Text("Average usage: " + controller.getAverageUsage(svcType)));
            serviceCardChildren.add(new Text("Total usage: " + controller.getTotalUsage(svcType)));
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
                    controller.getSelectedContracts().add((Contract) checkBox.getUserData());
                    return;
                }
                controller.getSelectedContracts().remove(checkBox.getUserData());
            });
            return checkBox;
        }).collect(Collectors.toList()));
        this.getContractsPane().visibleProperty().set(this.getContractsPane().getChildren().size() >= 1);
    }
}
