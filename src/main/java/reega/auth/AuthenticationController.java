/**
 *
 */
package reega.auth;

import java.util.Optional;

import reega.users.GenericUser;
import reega.users.NewUser;

/**
 * @author Marco
 *
 */
public interface AuthenticationController {

    /**
     * Try a login without a password
     *
     * @return an empty Optional if the operation didn't succeed, filled in with the
     *         logged in user otherwise
     */
    Optional<GenericUser> tryLoginWithoutPassword();

    /**
     * Create a new user
     *
     * @param user user to create
     * @return true if the user has been successfully added, false otherwise
     */
    boolean createUser(final NewUser user);

    /**
     * Login with the email and the password
     *
     * @param email     email to use for login
     * @param pwd       password to use for login
     * @param saveToken true if there's desire to save a token for a no password
     *                  login, false otherwise
     * @return an Optional filled in with logged in user if the login succeded, an
     *         empty Optional otherwise
     */
    Optional<GenericUser> emailLogin(String email, String pwd, boolean saveToken);

    /**
     * Login with the fiscal code and the password
     *
     * @param fiscalCode fiscal code to use for login
     * @param pwd        password to use for login
     * @param saveToken  true if there's desire to save a token for a no password
     *                   login, false otherwise
     * @return an Optional filled in with logged in user if the login succeded, an
     *         empty Optional otherwise
     */
    Optional<GenericUser> fiscalCodeLogin(String fiscalCode, String pwd, boolean saveToken);

}
