package reega.data.factory;

import org.jetbrains.annotations.Nullable;

import reega.data.ContractController;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteContractAPI;

/**
 * This factory returns an implementation of {@link ContractController} based on the needs.
 */
public final class ContractControllerFactory {
    private ContractControllerFactory() {
    }

    /**
     * Get the default {@link ContractController}.
     *
     * @param connection {@link RemoteConnection} used to get the default contract controller
     * @return the default {@link ContractController}
     */
    public static ContractController getDefaultDataController(@Nullable final RemoteConnection connection) {
        return new RemoteContractAPI(connection);
    }

    /**
     * Get the remote {@link ContractController}.
     *
     * @param connection {@link RemoteConnection} used to get the remote contract controller
     * @return the remote {@link ContractController}
     */
    public static ContractController getRemoteDatabaseController(@Nullable final RemoteConnection connection) {
        return new RemoteContractAPI(connection);
    }
}
