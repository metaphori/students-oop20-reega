package reega.controllers;

import reega.data.models.Contract;
import reega.users.User;
import reega.viewutils.Controller;
import reega.viewutils.EventHandler;

public interface SearchUserController extends Controller {
    /**
     * Set the user found event handler. <para>The user found event handler needs to be invoked whenever a valid user
     * is found</para>
     *
     * @param userEventHandler event handler
     */
    void setUserFoundEventHandler(EventHandler<User> userEventHandler);
    /**
     * Set the contract found event handler. <para>The contract found event handler needs to be invoked whenever a valid contract
     * is found</para>
     *
     * @param userEventHandler event handler
     */
    void setContractFoundEventHandler(EventHandler<Contract> userEventHandler);
}
