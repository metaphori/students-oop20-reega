package reega.data;

import java.sql.SQLException;

import reega.data.local.LocalAuth;
import reega.data.remote.RemoteAuthAPI;

/**
 * This factory returns an implementation of AuthController based on the needs.
 */
public final class AuthControllerFactory {
    public static AuthController getDefaultAuthController() throws ClassNotFoundException, SQLException {
        return new LocalAuth();
    }

    public static AuthController getLocalAuthController() throws ClassNotFoundException, SQLException {
        return new LocalAuth();
    }

    public static AuthController getRemoteAuthController() {
        return RemoteAuthAPI.getInstance();
    }
}
