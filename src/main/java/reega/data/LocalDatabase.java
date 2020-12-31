package reega.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
	public GenericUser emailLogin(String email, String hash) throws SQLException {
		return genericLogin("email", email, hash);
	}

	@Override
	public GenericUser fiscalCodeLogin(String fiscalCode, String hash) throws SQLException {
		return genericLogin("fiscal_code", fiscalCode, hash);
	}

	private GenericUser genericLogin(String key, String value, String hash) throws SQLException {
		final String sql = String.format("SELECT * FROM users WHERE \"%s\" = '%s' AND \"password\" = '%s';", key, value,
				hash);
		final Statement s = c.createStatement();
		var rs = s.executeQuery(sql);
		if (!rs.next()) {
			return null;
		}
		GenericUser user = new GenericUser(Role.valueOf(rs.getString("role").toUpperCase()), rs.getString("name"),
				rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
		rs.close();
		s.close();
		return user;
	}

}
