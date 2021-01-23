package reega.data;

import reega.data.local.LocalAuth;
import reega.data.remote.RemoteAuthAPI;

import java.sql.SQLException;

public final class AuthControllerFactory {
	public static AuthController getDefaultAuthController() throws ClassNotFoundException, SQLException {
		return new LocalAuth();
	}

	public static AuthController getLocalAuthController() throws ClassNotFoundException, SQLException {
		return new LocalAuth();
	}

	public static AuthController getRemoteAuthController() {
		return new RemoteAuthAPI();
	}
}
