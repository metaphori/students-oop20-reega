package reega.controllers;


import reega.data.models.Contract;
import reega.viewutils.EventHandler;

import java.util.List;

public interface UserProfileViewModel extends UserViewModel {
    /**
     * Set the contracts of the user
     * @param contracts contracts of the user
     */
    void setUserContracts(List<Contract> contracts);

    /**
     * Get the user contracts
     * @return the user contracts
     */
    List<Contract> getUserContracts();

    /**
     * Delete the current user contract
     * @param contract contract to remove
     */
    void deleteUserContract(Contract contract);

    /**
     * Set the delete user contract handler
     * @param deleteUserContractHandler delete user contract event handler
     */
    void setDeleteUserContractHandler(EventHandler<Contract> deleteUserContractHandler);
}
