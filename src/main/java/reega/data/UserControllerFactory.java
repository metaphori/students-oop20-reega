package reega.data;


import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteUserAPI;

import javax.annotation.Nullable;

public class UserControllerFactory {
    public static UserController getDefaultUserController(@Nullable RemoteConnection connection) {
        return new RemoteUserAPI(connection);
    }

    public static UserController getRemoteUserController(@Nullable RemoteConnection connection) {
        return new RemoteUserAPI(connection);
    }
}
