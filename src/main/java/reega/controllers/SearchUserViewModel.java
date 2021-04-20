package reega.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.models.Contract;
import reega.users.User;
import reega.viewutils.EventHandler;
import reega.viewutils.ViewModel;

public interface SearchUserViewModel extends ViewModel {

    /**
     * Search for the specified users.
     *
     * @param user the identifier of the user
     * @return a list of matching users
     */
    List<User> searchUser(String user);

    /**
     * Search for specified contract.
     *
     * @param contract identifier of the contract
     * @return a map containing users as keys and a set of their contracts as values
     */
    Map<User, Set<Contract>> searchContract(String contract);

    /**
     * Execute a certain action based on the passed user.
     *
     * @param user user that has been found
     */
    void setUserFound(User user);

    /**
     * Execute a certain action based on the passed contract.
     *
     * @param user     user owning <code>contract</code>
     * @param contract contract found
     */
    void setContractFound(User user, Contract contract);

    /**
     * Set the user found event handler. The user found event handler needs to be invoked whenever a valid user is found
     *
     * @param userEventHandler event handler
     */
    void setUserFoundEventHandler(EventHandler<User> userEventHandler);

    /**
     * Set the contract found event handler. The contract found event handler needs to be invoked whenever a valid
     * contract is found
     *
     * @param contractEventHandler contract found event handler
     */
    void setContractFoundEventHandler(EventHandler<Pair<User, Contract>> contractEventHandler);
}
