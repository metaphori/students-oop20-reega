package reega.data.local;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.security.crypto.bcrypt.BCrypt;

import reega.data.AuthController;
import reega.data.models.UserAuth;
import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

public final class LocalAuth implements AuthController {
    private final DBAccess db;

    public LocalAuth() throws ClassNotFoundException, SQLException {
        db = new DBAccess().getInstance();
    }

    @Override
    public void addUser(NewUser newUser) throws SQLException {
        String sql = "INSERT INTO users(\"fiscal_code\",\"name\",\"surname\",\"email\",\"password\",\"role\")";
        sql += String.format("VALUES('%s','%s','%s','%s','%s','%s');", newUser.getFiscalCode(), newUser.getName(),
                newUser.getSurname(), newUser.getEmail(), newUser.getPasswordHash(), newUser.getRole().getRoleName());
        db.executeStatement(sql);
    }

    @Override
    public GenericUser emailLogin(String email, String password) throws SQLException {
        return genericLogin("email", email, password);
    }

    @Override
    public GenericUser fiscalCodeLogin(String fiscalCode, String password) throws SQLException {
        return genericLogin("fiscal_code", fiscalCode, password);
    }

    @Override
    public GenericUser tokenLogin(UserAuth credentials) throws SQLException, IOException {
        String sql = String.format(db.getQuery("token_login.sql"),
                credentials.getSelector(), credentials.getValidator());
        final Statement s = db.getConnection().createStatement();
        ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final GenericUser user = new GenericUser(rs.getInt("id"), Role.valueOf(rs.getString("role").toUpperCase()),
                rs.getString("name"), rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
        rs.close();
        s.close();
        return user;
    }

    private GenericUser genericLogin(String key, String value, String password) throws SQLException {
        final String sql = String.format("SELECT * FROM users WHERE \"%s\" = '%s';", key, value);
        return getGenericUserFromQuery(sql, password);
    }

    private GenericUser getGenericUserFromQuery(String sql, String password) throws SQLException {
        final Statement s = db.getConnection().createStatement();
        final ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final String passwordHash = rs.getString("password");
        if (!BCrypt.checkpw(password, passwordHash)) {
            return null;
        }
        final GenericUser user = new GenericUser(rs.getInt("id"), Role.valueOf(rs.getString("role").toUpperCase()),
                rs.getString("name"), rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
        rs.close();
        s.close();
        return user;
    }

    @Override
    public void storeUserCredentials(int userID, String selector, String validator) throws IOException, SQLException {
        if (selector.length() > 12) {
            throw new IllegalArgumentException("The selector must be <= 12 characters");
        }
        if (validator.length() != 64) {
            throw new IllegalArgumentException("The validator bus be a 64 char string (result of SHA265 encryption)");
        }
        String sql = String.format(db.getQuery("insert_authentication.sql"), userID, selector, validator);
        db.executeStatement(sql);
    }

    @Override
    public void userLogout(int userID) throws SQLException {
        String sql = String.format("delete from authentication where user_id = %d;", userID);
        db.executeStatement(sql);
    }

}
