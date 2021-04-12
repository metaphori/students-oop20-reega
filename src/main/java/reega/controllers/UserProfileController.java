package reega.controllers;


import reega.data.models.Contract;

import java.util.List;

public interface UserProfileController extends UserController {
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
}
