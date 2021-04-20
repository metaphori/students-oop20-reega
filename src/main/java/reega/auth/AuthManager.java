/**
 *
 */
package reega.auth;

import java.util.Optional;

import reega.users.NewUser;
import reega.users.User;
import reega.util.ValueResult;

/**
 *
 */
public interface AuthManager {

    /**
     * Try a login without a password.
     *
     * @return a valid {@link ValueResult} if the operation succeeded correctly, an invalid one otherwise. Contains a
     *         non-empty {@link Optional} if you can login without the password, an empty optional otherwise
     */
    ValueResult<Optional<User>> tryLoginWithoutPassword();

    /**
     * Create a new user.
     *
     * @param user user to create
     * @return a valid {@link ValueResult} if the operation succeeded correctly, an invalid one otherwise
     */
    ValueResult<Void> createUser(NewUser user);

    /**
     * Login with the email and the password.
     *
     * @param email     email to use for login
     * @param pwd       password to use for login
     * @param saveToken true if there's desire to save a token for a no password login, false otherwise
     * @return a valid {@link ValueResult} if the operation succeeded correctly, an invalid one otherwise. Contains a
     *         non-empty {@link Optional} if you can login with email, an empty optional otherwise
     */
    ValueResult<Optional<User>> emailLogin(String email, String pwd, boolean saveToken);

    /**
     * Login with the fiscal code and the password.
     *
     * @param fiscalCode fiscal code to use for login
     * @param pwd        password to use for login
     * @param saveToken  true if there's desire to save a token for a no password login, false otherwise
     * @return a valid {@link ValueResult} if the operation succeeded correctly, an invalid one otherwise. Contains a
     *         non-empty {@link Optional} if you can login with the fiscal code, an empty optional otherwise
     */
    ValueResult<Optional<User>> fiscalCodeLogin(String fiscalCode, String pwd, boolean saveToken);

    /**
     * Log out the user represented by {@code userID} from the current application, and delete token if exists.
     *
     * @return a valid {@link ValueResult} if the operation succeeded correctly, an invalid one otherwise
     */
    ValueResult<Void> logout();

}
