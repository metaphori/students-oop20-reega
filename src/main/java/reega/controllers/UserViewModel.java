package reega.controllers;

import reega.users.User;
import reega.viewutils.ViewModel;

/**
 * ViewModel that contains the method for setting and getting a user
 */
public interface UserViewModel extends ViewModel {
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
