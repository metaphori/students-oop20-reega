package reega.data;

import reega.data.remote.RemoteAuthAPI;
import reega.data.remote.RemoteConnection;

import javax.annotation.Nullable;

/**
 * This factory returns an implementation of AuthController based on the needs.
 */
public final class AuthControllerFactory {
    public static AuthController getDefaultAuthController(@Nullable RemoteConnection connection) {
        return new RemoteAuthAPI(connection);
    }

    public static AuthController getRemoteAuthController(@Nullable RemoteConnection connection) {
        return new RemoteAuthAPI(connection);
    }
}
