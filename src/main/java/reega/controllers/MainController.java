/**
 *
 */
package reega.controllers;

import javafx.beans.property.ObjectProperty;
import reega.users.User;
import reega.viewutils.Controller;

/**
 * Interface for a controller that is represented by the page after the login
 *
 * @author Marco
 *
 */
public interface MainController extends Controller {

    ObjectProperty<User> user();
}
