package reega.controllers;

import reega.users.User;
import reega.viewutils.Controller;
import reega.viewutils.EventHandler;

public interface SearchUserController extends Controller {
    /**
     * Set the user found event handler
     * <para>The user found event handler needs to be invoked whenever a valid is user is found</para>
     * @param userEventHandler event handler
     */
    void setUserFoundEventHandler(EventHandler<User> userEventHandler);
}
