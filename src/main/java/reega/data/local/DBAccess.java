package reega.data.local;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

/**
 * Handle the connection to the local DB
 */
class DBAccess {
	private static DBAccess INSTANCE;
	private static final String dbURL = "jdbc:postgresql://localhost:5432/reega";
	private static final String dbUser = "postgres";
	private static final String dbPass = "Nze9IiKV4s31YacJa6r4AZBV";
	private static Connection c;
	protected Integer userID;

	DBAccess() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(dbURL, dbUser, dbPass);
		c.setAutoCommit(false);
	}

	protected synchronized DBAccess getInstance() throws ClassNotFoundException, SQLException {
		if (INSTANCE == null) {
			INSTANCE = new DBAccess();
		}
		return INSTANCE;
	}

	protected synchronized Connection getConnection() {
		return c;
	}

	protected synchronized void executeStatement(String sql) throws SQLException {
		final Statement s = c.createStatement();
		s.execute(sql);
		c.commit();
		s.close();
	}

	protected String getQuery(String queryFile) throws IOException {
		final InputStream is = getClass().getClassLoader().getResourceAsStream("queries/" + queryFile);
		assert is != null;
		return IOUtils.toString(is, StandardCharsets.UTF_8.name());
	}
}