package reega.data;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.PriceModel;

public final class LocalDatabase implements DataController {
    private final DBAccess db;

    protected LocalDatabase() throws ClassNotFoundException, SQLException {
        this.db = new DBAccess().getInstance();
    }

    @Override
    public List<Contract> getUserContracts(final int userID) throws IOException, SQLException {
        final String sql = String.format(this.db.getQuery("user_contracts.sql"), userID);

        final Statement s = this.db.getConnection().createStatement();
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

    @Override
    public void putUserData(final Data data) throws SQLException {
        final StringBuilder base = new StringBuilder(
                "insert into data (\"type\",\"value\",\"contract_id\",\"timestamp\") values ");

        for (final Entry<Long, Double> record : data.getData().entrySet()) {
            final ZonedDateTime d = Instant.ofEpochMilli(record.getKey()).atZone(ZoneOffset.UTC);
            base.append(String.format(Locale.US, "(%d, %f, %d, '%tF %tT'),", data.getType().getID(), record.getValue(),
                    data.getContractID(), d, d));
        }
        // remove trailing ","
        base.deleteCharAt(base.lastIndexOf(","));
        this.db.executeStatement(base.toString());
    }

    @Override
    public Long getLatestData(final int contractID) throws SQLException {
        final String sql = String.format(
                "select \"timestamp\" from  data where \"contract_id\" = %d order by \"timestamp\" desc limit 1;",
                contractID);
        final Statement s = this.db.getConnection().createStatement();
        final ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return new Date(rs.getTimestamp("timestamp", tzUTC).getTime()).getTime();
    }
}
