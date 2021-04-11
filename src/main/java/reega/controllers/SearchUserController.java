package reega.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import reega.data.models.Contract;
import reega.users.User;
import reega.viewutils.Controller;
import reega.viewutils.EventHandler;

public interface SearchUserController extends Controller {

    /**
     * Search for the specified users
     *
     * @param user the identifier of the user
     * @return a list of matching users
     */
    List<User> searchUser(String user);

    /**
     * Search for specified contract
     *
     * @param contract identifier of the contract
     * @return a list of matching contracts
     */
    Map<User, Set<Contract>> searchContract(String contract);

    /**
     * execute a certain action based on the passed user
     *
     * @param user
     */
    void setUserFound(User user);

    /**
     * execute a certain action based on the passed contract
     *
     * @param user
     * @param contract
     */
    void setContractFound(User user, Contract contract);

    /**
     * Set the user found event handler. <para>The user found event handler needs to be invoked whenever a valid user is
     * found</para>
     *
     * @param userEventHandler event handler
     */
    void setUserFoundEventHandler(EventHandler<User> userEventHandler);

    /**
     * Set the contract found event handler. <para>The contract found event handler needs to be invoked whenever a valid
     * contract is found</para>
     *
     * @param userEventHandler event handler
     */
    void setContractFoundEventHandler(EventHandler<Contract> userEventHandler);
}
