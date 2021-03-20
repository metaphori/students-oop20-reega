package reega.data.mock;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public final class RequestDispatcher extends Dispatcher {
    private final MockedDataService dataService;
    private final MockedAuthService authService;

    public RequestDispatcher(@Nullable final MockedDataService dataService,
            @Nullable final MockedAuthService authService) {
        this.dataService = dataService;
        this.authService = authService;
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull final RecordedRequest recordedRequest) {
        String path = Objects.requireNonNull(recordedRequest.getPath());
        final int queryPoint = path.indexOf("?");
        path = path.substring(0, queryPoint == -1 ? path.length() : queryPoint);
        try {
            if (path.startsWith("/auth/") && this.authService != null) {
                switch (path.substring(6)) {
                    case "addUser":
                        return this.authService.addUser(recordedRequest);
                    case "removeUser":
                        return authService.removeUser(recordedRequest);
                    case "emailLogin":
                        return this.authService.emailLogin(recordedRequest);
                    case "fcLogin":
                        return this.authService.fcLogin(recordedRequest);
                    case "tokenLogin":
                        return this.authService.tokenLogin(recordedRequest);
                    case "storeUserToken":
                        return this.authService.storeUserToken(recordedRequest);
                    case "logout":
                        return this.authService.logout();
                }
            } else if (path.startsWith("/data/") && this.dataService != null) {
                switch (path.substring(6)) {
                    case "contract":
                        return this.dataService.contracts();
                    case "fillUserData":
                        return this.dataService.fillData(recordedRequest);
                    case "getLatestTimestamp":
                        return this.dataService.getLatestTimestamp(recordedRequest);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println("route non trovata per" + recordedRequest.getPath());
        return new MockResponse().setResponseCode(500);
    }
}
