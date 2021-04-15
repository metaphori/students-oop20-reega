package reega.controllers;

import reega.data.models.gson.NewContract;
import reega.users.User;
import reega.viewutils.EventHandler;

import java.util.Optional;

public interface ContractCreationController {
        /**
         * Get the currently set {@link User} for this instance
         *
         * @return an User
         */
        Optional<User> getUser();

        /**
         * Set the current {@link User} for this instance
         *
         * @param user the user this instance will be linked with
         */
        void setUser(User user);

        /**
         * Register a new contract associated with the current user
         *
         * @param contract the new contract which will be registered
         * @return true if the contract has been registered successfully false otherwise
         * @throws IllegalArgumentException if the given contract isn't valid
         */
        boolean registerContract(NewContract contract) throws IllegalArgumentException;

        /**
         * Set the contract created event handler. The contract created event handler needs to be invoked whenever a valid
         * contract is created</para>
         *
         * @param eventHandler event handler
         */
        void setContractCreateEventHandler(EventHandler<NewContract> eventHandler);
}
