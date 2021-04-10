package reega.data;

import org.jetbrains.annotations.Nullable;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteDatabaseAPI;

/**
 * This factory returns an implementation of DataController based on the needs.
 */
public final class DataControllerFactory {
    public static DataController getDefaultDataController(@Nullable RemoteConnection connection) {
        return new RemoteDatabaseAPI(connection);
    }

    public static DataController getRemoteDatabaseController(@Nullable RemoteConnection connection) {
        return new RemoteDatabaseAPI(connection);
    }
}
