/**
 *
 */
package reega.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.users.User;
import reega.viewutils.Command;
import reega.viewutils.Controller;
import reega.viewutils.EventHandler;

/**
 * Interface for a controller that is represented by the page after the login
 *
 * @author Marco
 *
 */
public interface MainController extends Controller {
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

    /**
     * User property
     *
     * @return the user property that contains the current user
     */
    ObjectProperty<User> user();

    /**
     * Get the peek usage
     *
     * @param svcType service type used to get the average usage
     * @return the date(day) of the peek usage and the value of the peek
     */
    Optional<Pair<Date, Double>> getPeek(ServiceType svcType);

    /**
     * Get the average usage
     *
     * @param svcType service type used to get the average usage
     * @return the average usage (by day)
     */
    double getAverageUsage(ServiceType svcType);

    /**
     * Get the total usage
     *
     * @param svcType service type used to get the total usage
     * @return the total usage (by day)
     */
    double getTotalUsage(ServiceType svcType);

    /**
     * Get all the available service types for the current selected contracts
     *
     * @return a set of the available service types
     */
    Set<ServiceType> getAvailableServiceTypes();

    /**
     * Get the selected contracts of the {@link #user()}
     *
     * @return the selected contracts of the {@link #user()}
     */
    ObservableList<Contract> getSelectedContracts();

    /**
     * Get all the contracts of the user
     *
     * @return all the contracts of the user
     */
    List<Contract> getContracts();

    /**
     * Get all the commands available for the user
     *
     * @return a map containing the label as the key and the command as the value
     */
    Map<String, Command> getCommands();

    /**
     * Add <code>contract</code> to the selected contracts
     * @param contract contract to be marked as selected
     */
    void addSelectedContract(Contract contract);

    /**
     * Remove <code>contract</code> from the selected contracts
     * @param contract contract to be unmarked as selected
     */
    void removeSelectedContract(Contract contract);
}
