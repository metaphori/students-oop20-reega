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
		ResultSet rs = s.executeQuery(sql);
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

	@Override
	public List<Contract> getUserContracts(int userID) throws IOException, SQLException {
		final InputStream is = getClass().getClassLoader().getResourceAsStream("queries/user_contracts.sql");
		final String sql = String.format(IOUtils.toString(is, StandardCharsets.UTF_8.name()), userID);

		final Statement s = c.createStatement();
		ResultSet rs = s.executeQuery(sql);
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
}
