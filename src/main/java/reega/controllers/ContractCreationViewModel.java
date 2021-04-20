package reega.controllers;

import reega.data.models.Contract;
import reega.data.models.gson.NewContract;
import reega.viewutils.EventHandler;

public interface ContractCreationViewModel extends UserViewModel {

    /**
     * Register a new contract associated with the current user.
     *
     * @param contract the new contract which will be registered
     * @return true if the contract has been registered successfully false otherwise
     * @throws IllegalArgumentException if the given contract isn't valid
     */
    boolean registerContract(NewContract contract);

    /**
     * Set the contract created event handler. The contract created event handler needs to be invoked whenever a valid
     * contract is created
     *
     * @param eventHandler event handler
     */
    void setContractCreateEventHandler(EventHandler<Contract> eventHandler);
}
