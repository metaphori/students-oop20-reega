/**
 *
 */
package reega.data;

import java.util.Optional;

import reega.auth.AuthenticationController;
import reega.auth.RemindableAuthController;
import reega.logging.ExceptionHandler;
import reega.logging.SimpleExceptionHandler;
import reega.users.GenericUser;

/**
 * @author Marco
 *
 */
public class MainClass {

    /**
     * @param args
     */
    public static void main(final String[] args) throws Exception {

        final DataController dataController = LocalDatabase.getInstance();
        final ExceptionHandler exceptionHandler = new SimpleExceptionHandler();
        final AuthenticationController authController = new RemindableAuthController(dataController, exceptionHandler);

        final Optional<GenericUser> loggedInUser = authController.emailLogin("salomone@reega.it", "PSW", true);
        System.out.println(loggedInUser.get());
    }
}
