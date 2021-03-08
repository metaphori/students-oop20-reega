package reega.data;

import reega.data.local.LocalDatabase;
import reega.data.remote.RemoteDatabaseAPI;

import java.sql.SQLException;

/**
 * This factory returns an implementation of DataController based on the needs.
 */
public final class DataControllerFactory {
    public static DataController getDefaultDataController() throws ClassNotFoundException, SQLException {
        return RemoteDatabaseAPI.getInstance();
    }

    public static DataController getLocalDatabaseController() throws ClassNotFoundException, SQLException {
        return new LocalDatabase();
    }

    public static DataController getRemotelDatabaseController() {
        return RemoteDatabaseAPI.getInstance();
    }
}
