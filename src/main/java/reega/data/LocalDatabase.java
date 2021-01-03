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
        LocalDatabase.c = DriverManager.getConnection(LocalDatabase.dbURL, LocalDatabase.dbUser, LocalDatabase.dbPass);
        LocalDatabase.c.setAutoCommit(false);
    }

    protected synchronized static LocalDatabase getInstance() throws ClassNotFoundException, SQLException {
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
        this.executeStatement(sql);
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
    public GenericUser tokenLogin(final UserAuth credentials) throws SQLException {
        final String sql = String.format(
                "select u.* from users u inner join authentication a on u.id = a.user_id where u.\"id\" = %d;",
                credentials.getUserID());
        final Statement s = LocalDatabase.c.createStatement();
        final ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final GenericUser user = new GenericUser(rs.getInt("id"), Role.valueOf(rs.getString("role").toUpperCase()),
                rs.getString("name"), rs.getString("surname"), rs.getString("email"), rs.getString("fiscal_code"));
        rs.close();
        s.close();
        return user;
    }

    private GenericUser genericLogin(final String key, final String value, final String password) throws SQLException {
        final String sql = String.format("SELECT * FROM users WHERE \"%s\" = '%s';", key, value);
        return this.getGenericUserFromQuery(sql, password);
    }

    private GenericUser getGenericUserFromQuery(final String sql, final String password) throws SQLException {
        final Statement s = LocalDatabase.c.createStatement();
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
    public void storeUserCredentials(final int userID, final String selector, final String validator)
            throws IOException, SQLException {
        if (selector.length() > 12) {
            throw new IllegalArgumentException("The selector must be <= 12 characters");
        }
        if (validator.length() != 64) {
            throw new IllegalArgumentException("The validator bus be a 64 char string (result of SHA265 encryption)");
        }
        final String sql = String.format(this.getQuery("insert_authentication.sql"), userID, selector, validator);
        this.executeStatement(sql);
    }

    @Override
    public void userLogout(final int userID) throws SQLException {
        final String sql = String.format("delete from authentication where user_id = %d;", userID);
        this.executeStatement(sql);
    }

    @Override
    public List<Contract> getUserContracts(final int userID) throws IOException, SQLException {
        final String sql = String.format(this.getQuery("user_contracts.sql"), userID);

        final Statement s = LocalDatabase.c.createStatement();
        final ResultSet rs = s.executeQuery(sql);
        final Type pricesType = new TypeToken<Map<String, Double>>() {
        }.getType();
        final Type servicesType = new TypeToken<List<String>>() {
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

    private synchronized void executeStatement(final String sql) throws SQLException {
        final Statement s = LocalDatabase.c.createStatement();
        s.execute(sql);
        LocalDatabase.c.commit();
        s.close();
    }

    private String getQuery(final String queryFile) throws IOException {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("queries/" + queryFile);
        return IOUtils.toString(is, StandardCharsets.UTF_8.name());
    }
}
