package reega.data.factory;

import org.jetbrains.annotations.Nullable;

import reega.data.DataController;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteDataAPI;

/**
 * This factory returns an implementation of {@link DataController} based on the needs.
 */
public final class DataControllerFactory {
    private DataControllerFactory() {
    }

    /**
     * Get the default {@link DataController}.
     *
     * @param connection {@link RemoteConnection} used to get the default data controller
     * @return the default {@link DataController}
     */
    public static DataController getDefaultDataController(@Nullable final RemoteConnection connection) {
        return new RemoteDataAPI(connection);
    }

    /**
     * Get the remote {@link DataController}.
     *
     * @param connection {@link RemoteConnection} used to get the remote data controller
     * @return the remote {@link DataController}
     */
    public static DataController getRemoteDatabaseController(@Nullable final RemoteConnection connection) {
        return new RemoteDataAPI(connection);
    }

}
