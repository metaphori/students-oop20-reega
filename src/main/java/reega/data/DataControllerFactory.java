package reega.data;

import reega.data.local.LocalDatabase;
import reega.data.remote.RemoteDatabaseAPI;

import java.sql.SQLException;

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
