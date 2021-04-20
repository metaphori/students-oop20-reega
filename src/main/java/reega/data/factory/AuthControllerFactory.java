package reega.data.factory;

import javax.annotation.Nullable;

import reega.data.AuthController;
import reega.data.remote.RemoteAuthAPI;
import reega.data.remote.RemoteConnection;

/**
 * This factory returns an implementation of {@link AuthController} based on the needs.
 */
public final class AuthControllerFactory {
    private AuthControllerFactory() {
    }

    /**
     * Get the default {@link AuthController}.
     *
     * @param connection {@link RemoteConnection} used to get the default auth controller
     * @return the default {@link AuthController}
     */
    public static AuthController getDefaultAuthController(@Nullable final RemoteConnection connection) {
        return new RemoteAuthAPI(connection);
    }

    /**
     * Get the remote {@link AuthController}.
     *
     * @param connection {@link RemoteConnection} used to get the remote auth controller
     * @return the remote {@link AuthController}
     */
    public static AuthController getRemoteAuthController(@Nullable final RemoteConnection connection) {
        return new RemoteAuthAPI(connection);
    }
}
