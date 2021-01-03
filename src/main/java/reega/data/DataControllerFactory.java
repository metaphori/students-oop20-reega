package reega.data;

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
