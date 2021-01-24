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

    public RequestDispatcher(@Nullable MockedDataService dataService, @Nullable MockedAuthService authService) {
        this.dataService = dataService;
        this.authService = authService;
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
        String path = Objects.requireNonNull(recordedRequest.getPath());
        int queryPoint = path.indexOf("?");
        path = path.substring(0, queryPoint == -1 ? path.length() : queryPoint);
        try {
            if (path.startsWith("/auth/") && authService != null) {
                switch (path.substring(6)) {
                    case "addUser":
                        return authService.addUser(recordedRequest);
                    case "emailLogin":
                        return authService.emailLogin(recordedRequest);
                    case "fcLogin":
                        return authService.fcLogin(recordedRequest);
                    case "tokenLogin":
                        return authService.tokenLogin(recordedRequest);
                    case "storeUserToken":
                        return authService.storeUserToken(recordedRequest);
                    case "logout":
                        return authService.logout();
                }
            } else if (path.startsWith("/data/") && dataService != null) {
                switch (path.substring(6)) {
                    case "getContracts":
                        return dataService.contracts();
                    case "fillUserData":
                        return dataService.fillData(recordedRequest);
                    case "getLatestTimestamp":
                        return dataService.getLatestTimestamp(recordedRequest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("route non trovata per" + recordedRequest.getPath());
        return new MockResponse().setResponseCode(500);
    }
}
