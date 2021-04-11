package reega.controllers;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import reega.data.models.Contract;
import reega.users.User;

public interface OperatorMainController extends MainController {
    /**
     * Selected user property
     *
     * @return the selected user property that contains the selected user
     */
    ObjectProperty<User> selectedUser();

    /**
     * Get the selected user
     *
     * @return an Optional with the user inside if the {@link #selectedUser()} is not null, an empty {@link Optional}
     *         otherwise
     */
    Optional<User> getSelectedUser();

    /**
     * Jump to the search user controller
     */
    void jumpToSearchUser();

    /**
     * Remove the selected user
     */
    void removeSelectedUser();

    /**
     * Get the selected contracts of the {@link #selectedUser()}
     *
     * @return the selected contracts of the {@link #selectedUser()}, an empty list if the selected user is not set
     */
    @Override
    ObservableList<Contract> getSelectedContracts();

    /**
     * Get all the contracts of the {@link #selectedUser()}
     *
     * @return all the contracts of the {@link #selectedUser()}, an empty list if the selected user is not set
     */
    @Override
    List<Contract> getContracts();
}
