/**
 *
 */
package reega.controllers;

import javafx.collections.ObservableList;
import org.apache.commons.lang3.tuple.Pair;
import reega.data.models.Contract;
import reega.data.models.ServiceType;
import reega.statistics.DataPlotter;
import reega.viewutils.Command;
import reega.viewutils.EventHandler;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for a viewModel that is represented by the page after the login.
 *
 */
public interface MainViewModel extends UserViewModel {

    /**
     * Get the peek usage.
     *
     * @param svcType service type used to get the average usage
     * @return the date(day) of the peek usage and the value of the peek
     */
    Optional<Pair<Date, Double>> getPeek(ServiceType svcType);

    /**
     * Get the average usage.
     *
     * @param svcType service type used to get the average usage
     * @return the average usage (by day)
     */
    double getAverageUsage(ServiceType svcType);

    /**
     * Get the total usage.
     *
     * @param svcType service type used to get the total usage
     * @return the total usage (by day)
     */
    double getTotalUsage(ServiceType svcType);

    /**
     * Get all the available service types for the current selected contracts.
     *
     * @return a set of the available service types
     */
    Set<ServiceType> getAvailableServiceTypes();

    /**
     * Get the selected contracts of the {@link reega.users.User}.
     *
     * @return the selected contracts of the User
     */
    ObservableList<Contract> getSelectedContracts();

    /**
     * Get all the contracts of the user.
     *
     * @return all the contracts of the user
     */
    List<Contract> getContracts();

    /**
     * Get all the commands available for the user.
     *
     * @return a map containing the label as the key and the command as the value
     */
    ObservableList<Command> getCommands();

    /**
     * Add <code>contract</code> to the selected contracts.
     *
     * @param contract contract to be marked as selected
     */
    void addSelectedContract(Contract contract);

    /**
     * Remove <code>contract</code> from the selected contracts.
     *
     * @param contract contract to be unmarked as selected
     */
    void removeSelectedContract(Contract contract);

    /**
     * Get data plotter.
     *
     * @return the {@link DataPlotter}
     */
    DataPlotter getDataPlotter();

    /**
     * Set the event handler when the user wants to logout.
     *
     * @param evtHandler event handler for logout
     */
    void setOnLogout(EventHandler<Void> evtHandler);

    /**
     * Logout from the current application.
     */
    void logout();
}
