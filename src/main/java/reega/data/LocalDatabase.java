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
		c = DriverManager.getConnection(dbURL, dbUser, dbPass);
		c.setAutoCommit(false);
	}

	public synchronized static LocalDatabase getInstance() throws ClassNotFoundException, SQLException {
		if (INSTANCE == null) {
			INSTANCE = new LocalDatabase();
		}

		return INSTANCE;
	}

	@Override
	public void addUser(NewUser newUser) throws SQLException {
		String sql = "INSERT INTO users(\"fiscal_code\",\"name\",\"surname\",\"email\",\"password\",\"role\")";
		sql += String.format("VALUES('%s','%s','%s','%s','%s','%s');", newUser.getFiscalCode(), newUser.getName(),
				newUser.getSurname(), newUser.getEmail(), newUser.getPasswordHash(), newUser.getRole().getRoleName());
		final Statement s = c.createStatement();
		s.execute(sql);
		c.commit();
		s.close();
	}

	@Override
	public GenericUser emailLogin(String email, String password) throws SQLException {
		return genericLogin("email", email, password);
	}

	@Override
	public GenericUser fiscalCodeLogin(String fiscalCode, String password) throws SQLException {
		return genericLogin("fiscal_code", fiscalCode, password);
	}

	private GenericUser genericLogin(String key, String value, String password) throws SQLException {
		final String sql = String.format("SELECT * FROM users WHERE \"%s\" = '%s';", key, value);
		final Statement s = c.createStatement();
		var rs = s.executeQuery(sql);
		if (!rs.next()) {
			return null;
		}
		final String passwordHash = rs.getString("password");
		if (!BCrypt.checkpw(password, passwordHash)) {
			return null;
		}
		GenericUser user = new GenericUser(rs.getInt("id"), Role.valueOf(rs.getString("role").toUpperCase()),
				rs.getString("name"), rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
		rs.close();
		s.close();
		return user;
	}
}
