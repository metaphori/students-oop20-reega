package reega.data.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.PriceModel;
import reega.data.models.ServiceType;
import reega.data.remote.models.NewContract;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;

/**
 * Implementation of DataController, using a local database (mainly for development purpose)
 */
public final class LocalDatabase implements DataController {
    private final DBAccess db;

    public LocalDatabase() throws ClassNotFoundException, SQLException {
        this.db = new DBAccess().getInstance();
    }

    @Override
    public List<Contract> getUserContracts() throws IOException, SQLException {
        final String sql = String.format(this.db.getQuery("user_contracts.sql"), this.db.userID);

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
            contracts.add(new Contract(rs.getInt("contract_id"), rs.getString("address"), services, pm,
                    new Date(rs.getTimestamp("start_time", tzUTC).getTime())));
        }
        rs.close();
        s.close();
        return contracts;
    }

    @Override
    public List<Contract> getAllContracts() throws IOException, SQLException {
        return null;
    }

    @Override
    public void addContract(final NewContract contract) throws SQLException {
        final String sql = String.format(
                "with \"user\" as (select id from users where fiscal_code = %s)"
                        + "insert into contracts (user_id, address, price_model_id, services, start_time) ("
                        + "select id, '%s', %d, '%s', '%s' from \"user\");",
                contract.userFiscalCode, contract.address, contract.priceModelId, contract.services,
                contract.startTime);
        this.db.executeStatement(sql);
    }

    @Override
    public void removeContract(final int id) throws SQLException {
        this.db.executeStatement("delete from contracts where id = " + id);
    }

    @Override
    public List<PriceModel> getPriceModels() throws SQLException {
        final Statement s = this.db.getConnection().createStatement();
        final ResultSet rs = s.executeQuery("select * from price_models");
        final List<PriceModel> prices = new ArrayList<>();
        final Type pricesType = new TypeToken<Map<String, Double>>() {
        }.getType();
        while (rs.next()) {
            final Map<String, Double> p = new Gson().fromJson(rs.getString("prices"), pricesType);
            prices.add(new PriceModel(rs.getInt("id"), rs.getString("name"), p));
        }
        return prices;
    }

    @Override
    public void addPriceModel(final PriceModel priceModel) throws SQLException {
        final String prices = new Gson().toJson(priceModel.getPrices());
        final String sql = String.format("insert into price_models (\"name\", prices) values ('%s', '%s');",
                priceModel.getName(), prices);
        this.db.executeStatement(sql);
    }

    @Override
    public void removePriceModel(final int id) throws SQLException {
        this.db.executeStatement("delete from price_models where id = " + id);
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
    public Long getLatestData(final int contractID, final ServiceType service) throws SQLException {
        final String sql = String.format(
                "with latestData as (select \"timestamp\" from  data where \"contract_id\" = %d and \"type\" = %d order by \"timestamp\" desc limit 1)"
                        + "select coalesce((select * from latestData), (select start_time from contracts where id = %d))",
                contractID, service.getID(), contractID);
        final Statement s = this.db.getConnection().createStatement();
        final ResultSet rs = s.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return new Date(rs.getTimestamp("timestamp", tzUTC).getTime()).getTime();
    }
}
