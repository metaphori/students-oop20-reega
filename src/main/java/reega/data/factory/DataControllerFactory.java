package reega.data.factory;

import org.jetbrains.annotations.Nullable;
import reega.data.DataController;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteDataAPI;

public class DataControllerFactory {
    public static DataController getDefaultDataController(@Nullable RemoteConnection connection) {
        return new RemoteDataAPI(connection);
    }

    public static DataController getRemoteDatabaseController(@Nullable RemoteConnection connection) {
        return new RemoteDataAPI(connection);
    }

}
