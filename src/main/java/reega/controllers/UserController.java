package reega.controllers;

import javafx.beans.property.ObjectProperty;
import reega.users.User;
import reega.viewutils.Controller;

/**
 * Controller that contains the method for setting and getting a user
 */
public interface UserController extends Controller {
    /**
     * Set the current user
     *
     * @param newUser new user to set
     */
    void setUser(User newUser);

    /**
     * Get the current user
     *
     * @return the current user
     */
    User getUser();
}
