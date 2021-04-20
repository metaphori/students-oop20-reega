package reega.data.factory;

import javax.annotation.Nullable;

import reega.data.UserController;
import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteUserAPI;

/**
 * This factory returns an implementation of {@link UserController} based on the needs.
 */
public final class UserControllerFactory {
    private UserControllerFactory() {
    }

    /**
     * Get the default {@link UserController}.
     *
     * @param connection {@link RemoteConnection} used to get the default user controller
     * @return the default {@link UserController}
     */
    public static UserController getDefaultUserController(@Nullable final RemoteConnection connection) {
        return new RemoteUserAPI(connection);
    }

    /**
     * Get the remote {@link UserController}.
     *
     * @param connection {@link RemoteConnection} used to get the remote user controller
     * @return the remote {@link UserController}
     */
    public static UserController getRemoteUserController(@Nullable final RemoteConnection connection) {
        return new RemoteUserAPI(connection);
    }
}
