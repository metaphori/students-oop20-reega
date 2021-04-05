package reega.data;

import reega.data.remote.RemoteAuthAPI;
import reega.data.remote.RemoteConnection;

/**
 * This factory returns an implementation of AuthController based on the needs.
 */
public final class AuthControllerFactory {
    public static AuthController getDefaultAuthController() {
        return RemoteAuthAPI.getInstance();
    }

    public static AuthController getRemoteAuthController() {
        return RemoteAuthAPI.getInstance();
    }

    public static AuthController getRemoteAuthController(RemoteConnection connection) {
        return RemoteAuthAPI.getInstance(connection);
    }
}
