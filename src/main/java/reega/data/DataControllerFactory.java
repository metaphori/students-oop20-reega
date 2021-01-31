package reega.data;

import java.sql.SQLException;

import reega.data.local.LocalDatabase;
import reega.data.remote.RemoteDatabaseAPI;

/**
 * This factory returns an implementation of DataController based on the needs.
 */
public final class DataControllerFactory {
    public static DataController getDefaultDataController() throws ClassNotFoundException, SQLException {
        return new LocalDatabase();
    }

    public static DataController getLocalDatabaseController() throws ClassNotFoundException, SQLException {
        return new LocalDatabase();
    }

    public static DataController getRemotelDatabaseController() {
        return RemoteDatabaseAPI.getInstance();
    }
}
