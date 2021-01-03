package reega.data;

import java.sql.SQLException;

public final class DataControllerFactory {
	public static DataController getDefaultDataController() throws ClassNotFoundException, SQLException {
		return LocalDatabase.getInstance();
	}

	public static DataController getLocalDatabaseController() throws ClassNotFoundException, SQLException {
		return LocalDatabase.getInstance();
	}

	public static DataController getRemotelDatabaseController() throws ClassNotFoundException, SQLException {
		return RemoteDatabaseAPI.getInstance();
	}
}
