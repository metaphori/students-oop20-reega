package reega.views;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import reega.controllers.SearchUserController;
import reega.data.models.Contract;
import reega.users.User;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewutils.ViewUtils;

public class UserSearchView extends VBox {

    @FXML
    private TextField searchBar;
    @FXML
    private ToggleButton userSearch;
    @FXML
    private ToggleButton contractSearch;
    @FXML
    private Button searchButton;
    @FXML
    private FlexibleGridPane cardsPane;

    public UserSearchView(SearchUserController controller) {
        final FXMLLoader loader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("views/UserSearch.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        EventHandler<ActionEvent> action = e -> {
            if (this.userSearch.isSelected()) {
                this.populateCardBoxByUser(controller);
            } else {
                this.populateCardBoxByContract(controller);
            }
        };
        this.searchBar.setOnAction(action);
        this.searchButton.setOnAction(action);

        this.searchBar.setPromptText("Search for name, surname, fiscal code or email");
        this.userSearch.setOnAction(e -> searchBar.setPromptText("Search for name, surname, fiscal code or email"));
        this.contractSearch.setOnAction(e -> searchBar.setPromptText("Search for services, address or accountholder"));
    }

    protected void populateCardBoxByUser(SearchUserController controller) {
        List<User> users = controller.searchUser(this.searchBar.getText());
        // add cards with user information to the cardsBox
        this.cardsPane.getChildren().clear();
        this.cardsPane.getChildren().addAll(users.stream().map(user -> {
            Card card = ViewUtils.wrapNodeWithStyleClasses(new Card(), "search-card");
            card.getChildren()
                    .addAll(ViewUtils.wrapNodeWithStyleClasses(new Label("name: " + user.getFullName()), "search-text"),
                            ViewUtils.wrapNodeWithStyleClasses(new Label("fiscal code: " + user.getFiscalCode()),
                                    "search-text"),
                            ViewUtils.wrapNodeWithStyleClasses(new Label("email: " + user.getEmail()), "search-text"));
            card.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    controller.setUserFound(user);
                }
            });
            return card;
        }).collect(Collectors.toList()));
    }

    protected void populateCardBoxByContract(SearchUserController controller) {
        Map<User, Set<Contract>> contracts = controller.searchContract(this.searchBar.getText());
        // add cards with contract informations to the cardsBox
        this.cardsPane.getChildren().clear();
        this.cardsPane.getChildren().addAll(contracts.entrySet().stream().flatMap(userContracts -> {

            User user = userContracts.getKey();
            return userContracts.getValue().stream().map(contract -> {
                Card card = ViewUtils.wrapNodeWithStyleClasses(new Card(), "search-card");
                // add user info and contract info for each card
                card.getChildren()
                        .addAll(ViewUtils.wrapNodeWithStyleClasses(new Label("User"), "search-header"),
                                ViewUtils.wrapNodeWithStyleClasses(new Label("name: " + user.getFullName()),
                                        "search-text"),
                                ViewUtils.wrapNodeWithStyleClasses(new Label("fiscal code: " + user.getFiscalCode()),
                                        "search-text"),
                                ViewUtils.wrapNodeWithStyleClasses(new Label("Contract"), "search-header"),
                                ViewUtils
                                        .wrapNodeWithStyleClasses(new Label(
                                                "address: " + contract.getAddress()), "search-text"),
                                ViewUtils.wrapNodeWithStyleClasses(new Label("services: " + contract.getServices()
                                        .stream()
                                        .map(srv -> StringUtils.capitalize(srv.getName()))
                                        .collect(Collectors.joining(", "))), "search-text"));
                card.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        controller.setContractFound(user, contract);
                    }
                });
                return card;
            });
        }).collect(Collectors.toList()));
    }

}
