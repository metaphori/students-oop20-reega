package reega.controllers;

import javafx.collections.ObservableList;
import reega.data.models.Contract;
import reega.users.User;

import java.util.Optional;

public interface OperatorMainController extends MainController {
    Optional<User> getSelectedUser();
    Optional<ObservableList<Contract>> getSelectedContracts();
}
