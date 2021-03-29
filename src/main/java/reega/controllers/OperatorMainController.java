package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import reega.data.models.Contract;
import reega.users.User;

import java.util.List;
import java.util.Optional;

public interface OperatorMainController extends MainController {
    ObjectProperty<User> selectedUser();
    void setSelectedUser(User newUser);
    Optional<User> getSelectedUser();
    void jumpToSearchUser();
    void removeSelectedUser();
}
