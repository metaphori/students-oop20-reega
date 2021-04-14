package reega.data.factory;

import org.jetbrains.annotations.Nullable;
import reega.data.ContractController;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteContractAPI;

/**
 * This factory returns an implementation of DataController based on the needs.
 */
public final class ContractControllerFactory {
    public static ContractController getDefaultDataController(@Nullable RemoteConnection connection) {
        return new RemoteContractAPI(connection);
    }

    public static ContractController getRemoteDatabaseController(@Nullable RemoteConnection connection) {
        return new RemoteContractAPI(connection);
    }
}
