/**
 *
 */
package reega.auth;

import java.io.IOException;
import java.sql.SQLException;

import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

/**
 * @author Marco
 *
 */
public class MockAuthController implements AuthController {

    public static final int ID_FOR_SQL_EXCEPTION = 1;
    public static final String EMAIL_FOR_SQL_EXCEPTION = "Email";
    public static final int ID_FOR_IO_EXCEPTION = 2;
    public static final String HASH_SQL_EXCEPTION = "HASH";
    public static final String TOKEN_SQL_EXCEPTION = "SELECTOR";

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUser(final NewUser newUser) throws SQLException {
        if (newUser.getEmail().startsWith(MockAuthController.EMAIL_FOR_SQL_EXCEPTION)) {
            throw new SQLException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericUser emailLogin(final String email, final String hash) throws SQLException {
        if (hash.startsWith(MockAuthController.HASH_SQL_EXCEPTION)) {
            throw new SQLException();
        }

        return new GenericUser(Role.ADMIN, "Name", "Surname", email, "FC");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericUser fiscalCodeLogin(final String fiscalCode, final String hash) throws SQLException {
        if (hash.startsWith(MockAuthController.HASH_SQL_EXCEPTION)) {
            throw new SQLException();
        }

        return new GenericUser(Role.ADMIN, "Name", "Surname", "Email", fiscalCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericUser tokenLogin(final UserAuth credentials) throws SQLException {
        if (credentials.getSelector().startsWith(MockAuthController.TOKEN_SQL_EXCEPTION)) {
            throw new SQLException();
        }

        return new GenericUser(Role.ADMIN, "Name", "Surname", "Email", "Fiscal Code");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeUserCredentials(final String selector, final String validator)
            throws SQLException, IOException {
        // deprecated handling user ID
        /*if (userID == MockAuthController.ID_FOR_SQL_EXCEPTION) {
            throw new SQLException();
        } else if (userID == MockAuthController.ID_FOR_IO_EXCEPTION) {
            throw new IOException();
        }*/

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userLogout() throws SQLException {
        // deprecated handling user ID
        /*if (userID == MockAuthController.ID_FOR_SQL_EXCEPTION) {
            throw new SQLException();
        }*/
    }
}
