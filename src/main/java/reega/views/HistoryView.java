package reega.views;

import java.util.Locale;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import reega.controllers.HistoryViewModel;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewcomponents.WrappableLabel;
import reega.viewutils.ReegaFXMLLoader;
import reega.viewutils.ReegaView;
import reega.viewutils.ViewUtils;

public class HistoryView extends VBox implements ReegaView {
    @FXML
    private WrappableLabel contractsLabel;
    @FXML
    private VBox cardsList;

    public HistoryView(final HistoryViewModel historyViewModel) {
        ReegaFXMLLoader.loadFXML(this, "views/History.fxml");
        this.contractsLabel.setText(
                historyViewModel.getContracts().stream().map(Contract::getAddress).collect(Collectors.joining(", ")));

        this.cardsList.getChildren().addAll(historyViewModel.getValues().stream().map(v -> {
            final FlexibleGridPane fgp = ViewUtils.wrapNodeWithStyleClasses(new FlexibleGridPane(), "services-pane");
            fgp.setFixedColumnsNumber(3);
            fgp.setVisible(false);

            fgp.getChildren().addAll(v.getReports().entrySet().stream().map(r -> {
                final Card card = ViewUtils.wrapNodeWithStyleClasses(new Card(), "service-card");
                card.getChildren()
                        .addAll(ViewUtils.wrapNodeWithStyleClasses(
                                new WrappableLabel(r.getKey().getName().toUpperCase(Locale.US)), "service-header"),
                                new WrappableLabel("Total: " + r.getValue().getSum() + " "
                                        + ServiceType.getMeasurementUnit(r.getKey().getServiceType())),
                                new WrappableLabel("Average: " + r.getValue().getAvg() + " "
                                        + ServiceType.getMeasurementUnit(r.getKey().getServiceType())));

                return card;
            }).collect(Collectors.toList()));
            fgp.managedProperty().bind(fgp.visibleProperty());

            final Card c = new Card();
            final HBox box = new HBox();
            final Label monthLabel = ViewUtils.wrapNodeWithStyleClasses(new Label(v.getMonth()), "date-label");
            monthLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(monthLabel, Priority.ALWAYS);
            box.getChildren().add(monthLabel);
            final ToggleButton button = ViewUtils.wrapNodeWithStyleClasses(new ToggleButton(), "expand-button");
            button.setOnAction(e -> fgp.setVisible(!fgp.isVisible()));

            box.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    button.fire();
                }
            });

            box.getChildren().add(button);
            c.getChildren().addAll(box, fgp);

            return c;
        }).collect(Collectors.toList()));
    }
}
