/**
 *
 */
package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.tuple.Pair;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.users.User;
import reega.viewutils.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for a controller that is represented by the page after the login
 *
 * @author Marco
 *
 */
public interface MainController extends Controller {
    /**
     * Set the current user
     * @param newUser new user to set
     */
    void setUser(User newUser);
    User getUser();
    ObjectProperty<User> user();
    /**
     * Get the peek usage
     * @param svcType service type used to get the average usage
     * @return the date of the peek usage and the value of the peek
     */
    Optional<Pair<Date,Double>> getPeek(ServiceType svcType);

    /**
     * Get the average usage
     * @param svcType service type used to get the average usage
     * @return the average usage
     */
    double getAverageUsage(ServiceType svcType);

    /**
     * Get the total usage
     * @param svcType service type used to get the total usage
     * @return the total usage
     */
    double getTotalUsage(ServiceType svcType);

    Set<ServiceType> getAvailableServiceTypes();
    ObservableList<Contract> getSelectedContracts();
    List<Contract> getContracts();
}
