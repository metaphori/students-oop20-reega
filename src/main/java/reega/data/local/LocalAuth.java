package reega.data.local;

import org.springframework.security.crypto.bcrypt.BCrypt;
import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of AuthController, using a local database (mainly for development purpose)
 */
public final class LocalAuth implements AuthController {
    private final DBAccess db;

    public LocalAuth() throws ClassNotFoundException, SQLException {
        this.db = new DBAccess().getInstance();
    }

    @Override
    public void addUser(final NewUser newUser) throws SQLException {
        String sql = "INSERT INTO users(\"fiscal_code\",\"name\",\"surname\",\"email\",\"password\",\"role\")";
        sql += String.format("VALUES('%s','%s','%s','%s','%s','%s');", newUser.getFiscalCode(), newUser.getName(),
                newUser.getSurname(), newUser.getEmail(), newUser.getPasswordHash(), newUser.getRole().getRoleName());
        this.db.executeStatement(sql);
    }

    @Override
    public GenericUser emailLogin(final String email, final String password) throws SQLException {
        return this.genericLogin("email", email, password);
    }

    @Override
    public GenericUser fiscalCodeLogin(final String fiscalCode, final String password) throws SQLException {
        return this.genericLogin("fiscal_code", fiscalCode, password);
    }

    @Override
    public GenericUser tokenLogin(final UserAuth credentials) throws SQLException, IOException {
        final String sql = String.format(this.db.getQuery("token_login.sql"), credentials.getSelector(),
                credentials.getValidator());
        final Statement s = this.db.getConnection().createStatement();
        final ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final GenericUser user = new GenericUser(Role.valueOf(rs.getString("role").toUpperCase()), rs.getString("name"),
                rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
        rs.close();
        s.close();
        if (this.db.userID == null) {
            this.db.userID = rs.getInt("id");
        }
        return user;
    }

    private GenericUser genericLogin(final String key, final String value, final String password) throws SQLException {
        final String sql = String.format("SELECT * FROM users WHERE \"%s\" = '%s';", key, value);
        return this.getGenericUserFromQuery(sql, password);
    }

    private GenericUser getGenericUserFromQuery(final String sql, final String password) throws SQLException {
        final Statement s = this.db.getConnection().createStatement();
        final ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final String passwordHash = rs.getString("password");
        if (!BCrypt.checkpw(password, passwordHash)) {
            return null;
        }
        final GenericUser user = new GenericUser(Role.valueOf(rs.getString("role").toUpperCase()), rs.getString("name"),
                rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
        if (this.db.userID == null) {
            this.db.userID = rs.getInt("id");
        }
        rs.close();
        s.close();
        return user;
    }

    @Override
    public void storeUserCredentials(final String selector, final String validator) throws IOException, SQLException {
        if (selector.length() > 12) {
            throw new IllegalArgumentException("The selector must be <= 12 characters");
        }
        if (validator.length() != 64) {
            throw new IllegalArgumentException("The validator bus be a 64 char string (result of SHA265 encryption)");
        }
        final String sql = String.format(this.db.getQuery("insert_authentication.sql"), this.db.userID, selector,
                validator);
        this.db.executeStatement(sql);
    }

    @Override
    public void userLogout() throws SQLException {
        final String sql = String.format("delete from authentication where user_id = %d;", this.db.userID);
        this.db.executeStatement(sql);
    }

}
