package reega.views;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import reega.controllers.SearchUserViewModel;
import reega.data.models.Contract;
import reega.users.User;
import reega.viewcomponents.Card;
import reega.viewcomponents.FlexibleGridPane;
import reega.viewcomponents.WrappableLabel;
import reega.viewutils.ReegaFXMLLoader;
import reega.viewutils.ReegaView;
import reega.viewutils.ViewUtils;

public class UserSearchView extends VBox implements ReegaView {

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

    public UserSearchView(final SearchUserViewModel viewModel) {
        ReegaFXMLLoader.loadFXML(this, "views/UserSearch.fxml");

        final EventHandler<ActionEvent> action = e -> {
            if (this.userSearch.isSelected()) {
                this.populateCardBoxByUser(viewModel);
            } else {
                this.populateCardBoxByContract(viewModel);
            }
        };
        this.searchBar.setOnAction(action);
        this.searchButton.setOnAction(action);

        this.searchBar.setPromptText("Search for name, surname, fiscal code or email");
        this.userSearch
                .setOnAction(e -> this.searchBar.setPromptText("Search for name, surname, fiscal code or email"));
        this.contractSearch
                .setOnAction(e -> this.searchBar.setPromptText("Search for services, address or accountholder"));
    }

    /**
     * Populate the card box by user.
     *
     * @param viewModel viewmodel used to populate the card box
     */
    private void populateCardBoxByUser(final SearchUserViewModel viewModel) {
        final List<User> users = viewModel.searchUser(this.searchBar.getText());
        // add cards with user information to the cardsBox
        this.cardsPane.getChildren().clear();
        this.cardsPane.getChildren().addAll(users.stream().map(user -> {
            final Card card = ViewUtils.wrapNodeWithStyleClasses(new Card(), "search-card");
            card.getChildren()
                    .addAll(ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("name: " + user.getFullName()),
                            "search-text"),
                            ViewUtils.wrapNodeWithStyleClasses(
                                    new WrappableLabel("fiscal code: " + user.getFiscalCode()), "search-text"),
                            ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("email: " + user.getEmail()),
                                    "search-text"));
            card.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    viewModel.setUserFound(user);
                }
            });
            return card;
        }).collect(Collectors.toList()));
    }

    /**
     * Populate the card box by contract.
     *
     * @param viewModel viewmodel used to populate the card box
     */
    protected void populateCardBoxByContract(final SearchUserViewModel viewModel) {
        final Map<User, Set<Contract>> contracts = viewModel.searchContract(this.searchBar.getText());
        // add cards with contract informations to the cardsBox
        this.cardsPane.getChildren().clear();
        this.cardsPane.getChildren().addAll(contracts.entrySet().stream().flatMap(userContracts -> {

            final User user = userContracts.getKey();
            return userContracts.getValue().stream().map(contract -> {
                final Card card = ViewUtils.wrapNodeWithStyleClasses(new Card(), "search-card");
                // add user info and contract info for each card
                card.getChildren()
                        .addAll(ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("User"), "search-header"),
                                ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("name: " + user.getFullName()),
                                        "search-text"),
                                ViewUtils.wrapNodeWithStyleClasses(
                                        new WrappableLabel("fiscal code: " + user.getFiscalCode()), "search-text"),
                                ViewUtils.wrapNodeWithStyleClasses(new WrappableLabel("Contract"), "search-header"),
                                ViewUtils
                                        .wrapNodeWithStyleClasses(new WrappableLabel(
                                                "address: " + contract.getAddress()), "search-text"),
                                ViewUtils.wrapNodeWithStyleClasses(
                                        new WrappableLabel("services: " + contract.getServices()
                                                .stream()
                                                .map(srv -> StringUtils.capitalize(srv.getName()))
                                                .collect(Collectors.joining(", "))),
                                        "search-text"));
                card.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        viewModel.setContractFound(user, contract);
                    }
                });
                return card;
            });
        }).collect(Collectors.toList()));
    }

}
