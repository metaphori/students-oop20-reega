/**
 *
 */
package reega.controllers;

import reega.users.GenericUser;
import reega.viewutils.Controller;

/**
 * Interface for a controller that is represented by the page after the login
 *
 * @author Marco
 *
 */
public interface PageAfterLoginController extends Controller {
    /**
     * Set the user
     *
     * @param user user to set
     */
    void setUser(GenericUser user);
}
