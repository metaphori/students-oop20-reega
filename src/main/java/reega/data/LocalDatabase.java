package reega.data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import reega.data.models.Contract;
import reega.data.models.PriceModel;
import reega.data.models.UserAuth;
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
		executeStatement(sql);
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
	public GenericUser tokenLogin(UserAuth credentials) throws SQLException {
		String sql = String.format(
				"select u.* from users u inner join authentication a on u.id = a.user_id where u.\"id\" = %d;",
				credentials.getUserID());
		final Statement s = c.createStatement();
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
		final Statement s = c.createStatement();
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
		String sql = String.format(getQuery("insert_authentication.sql"), userID, selector, validator);
		executeStatement(sql);
	}

	@Override
	public void userLogout(int userID) throws SQLException {
		String sql = String.format("delete from authentication where user_id = %d;", userID);
		executeStatement(sql);
	}

	@Override
	public List<Contract> getUserContracts(int userID) throws IOException, SQLException {
		final String sql = String.format(getQuery("user_contracts.sql"), userID);

		final Statement s = c.createStatement();
		final ResultSet rs = s.executeQuery(sql);
		Type pricesType = new TypeToken<Map<String, Double>>() {
		}.getType();
		Type servicesType = new TypeToken<List<String>>() {
		}.getType();
		final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		final List<Contract> contracts = new ArrayList<>();
		while (rs.next()) {
			final Map<String, Double> prices = new Gson().fromJson(rs.getString("prices"), pricesType);
			final PriceModel pm = new PriceModel(rs.getInt("price_model_id"), rs.getString("price_model_name"), prices);
			final List<String> services = new Gson().fromJson(rs.getString("services"), servicesType);
			contracts.add(new Contract(rs.getInt("contract_id"), rs.getInt("user_id"), rs.getString("address"),
					services, pm, new Date(rs.getTimestamp("start_time", tzUTC).getTime())));
		}
		rs.close();
		s.close();
		return contracts;
	}

	private synchronized void executeStatement(String sql) throws SQLException {
		final Statement s = c.createStatement();
		s.execute(sql);
		c.commit();
		s.close();
	}

	private String getQuery(String queryFile) throws IOException {
		final InputStream is = getClass().getClassLoader().getResourceAsStream("queries/" + queryFile);
		return IOUtils.toString(is, StandardCharsets.UTF_8.name());
	}
}
