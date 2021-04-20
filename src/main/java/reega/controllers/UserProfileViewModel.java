package reega.controllers;

import java.util.List;

import reega.data.models.Contract;
import reega.users.User;
import reega.util.ValueResult;
import reega.viewutils.EventHandler;

public interface UserProfileViewModel extends UserViewModel {
    /**
     * Set the contracts of the user.
     *
     * @param contracts contracts of the user
     */
    void setUserContracts(List<Contract> contracts);

    /**
     * Get the user contracts.
     *
     * @return the user contracts
     */
    List<Contract> getUserContracts();

    /**
     * Delete the current user contract.
     *
     * @param contract contract to remove
     */
    void deleteUserContract(Contract contract);

    /**
     * Set the delete user contract handler.
     *
     * @param deleteUserContractHandler delete user contract event handler
     */
    void setDeleteUserContractHandler(EventHandler<Contract> deleteUserContractHandler);

    /**
     * Set the delete user handler.
     *
     * @param deleteUserHandler delete user handler
     */
    void setDeleteUserHandler(EventHandler<User> deleteUserHandler);

    /**
     * Delete the current user.
     *
     * @return Return a valid {@link ValueResult} if the deletion succeeded, an invalid one otherwise
     */
    ValueResult<Void> deleteCurrentUser();
}
