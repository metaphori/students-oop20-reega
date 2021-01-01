package reega.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.security.crypto.bcrypt.BCrypt;

import reega.users.GenericUser;
import reega.users.NewUser;
import reega.users.Role;

public final class LocalDatabase implements DataController {
    private static LocalDatabase INSTANCE;
    private static final String dbURL = "jdbc:postgresql://localhost:5432/reega";
    private static final String dbUser = "postgres";
    private static final String dbPass = "Nze9IiKV4s31YacJa6r4AZBV";
    private static Connection c;

    private LocalDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        LocalDatabase.c = DriverManager.getConnection(LocalDatabase.dbURL, LocalDatabase.dbUser, LocalDatabase.dbPass);
        LocalDatabase.c.setAutoCommit(false);
    }

    public synchronized static LocalDatabase getInstance() throws ClassNotFoundException, SQLException {
        if (LocalDatabase.INSTANCE == null) {
            LocalDatabase.INSTANCE = new LocalDatabase();
        }

        return LocalDatabase.INSTANCE;
    }

    @Override
    public void addUser(final NewUser newUser) throws SQLException {
        String sql = "INSERT INTO users(\"fiscal_code\",\"name\",\"surname\",\"email\",\"password\",\"role\")";
        sql += String.format("VALUES('%s','%s','%s','%s','%s','%s');", newUser.getFiscalCode(), newUser.getName(),
                newUser.getSurname(), newUser.getEmail(), newUser.getPasswordHash(), newUser.getRole().getRoleName());
        final Statement s = LocalDatabase.c.createStatement();
        s.execute(sql);
        LocalDatabase.c.commit();
        s.close();
    }

    @Override
    public GenericUser emailLogin(final String email, final String password) throws SQLException {
        return this.genericLogin("email", email, password);
    }

    @Override
    public GenericUser fiscalCodeLogin(final String fiscalCode, final String password) throws SQLException {
        return this.genericLogin("fiscal_code", fiscalCode, password);
    }

    private GenericUser genericLogin(final String key, final String value, final String password) throws SQLException {
        final String sql = String.format("SELECT * FROM users WHERE \"%s\" = '%s';", key, value);
        final Statement s = LocalDatabase.c.createStatement();
        final var rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final String passwordHash = rs.getString("password");
        if (!BCrypt.checkpw(password, passwordHash)) {
            return null;
        }
        final GenericUser user = new GenericUser(Role.valueOf(rs.getString("role").toUpperCase()), rs.getString("name"),
                rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
        rs.close();
        s.close();
        return user;
    }
}
