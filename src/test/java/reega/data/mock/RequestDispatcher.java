package reega.data.mock;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public final class RequestDispatcher extends Dispatcher {
    private final MockedDataService dataService;

    public RequestDispatcher(MockedDataService dataService) {
        this.dataService = dataService;
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
        String path = Objects.requireNonNull(recordedRequest.getPath());
        int queryPoint = path.indexOf("?");
        path = path.substring(0, queryPoint == -1 ? path.length() : queryPoint);
        try {
            switch (path) {
                // Auth
                case "/auth/addUser":
                    return addUser(recordedRequest);
                case "/auth/emailLogin":
                    return emailLogin(recordedRequest);
                case "/auth/fcLogin":
                    return fcLogin(recordedRequest);
                case "/auth/tokenLogin":
                    return tokenLogin(recordedRequest);
                case "/auth/storeUserToken":
                    return storeUserToken(recordedRequest);
                case "/auth/logout":
                    return logout(recordedRequest);
                // Data
                case "/data/getContracts":
                    return dataService.contracts();
                case "/data/fillUserData":
                    return dataService.fillData(recordedRequest);
                case "/data/getLatestTimestamp":
                    return dataService.getLatestTimestamp(recordedRequest);
                default:
                    System.out.println("route non trovata per" + recordedRequest.getPath());
                    return new MockResponse().setResponseCode(404);
            }
        } catch (IOException e) {
            return new MockResponse().setResponseCode(500);
        }
    }

    private MockResponse addUser(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse emailLogin(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse fcLogin(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse tokenLogin(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse storeUserToken(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse logout(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private String getResponse(String response) throws IOException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("mock-response/" + response);
        assert is != null;
        return IOUtils.toString(is, StandardCharsets.UTF_8.name());
    }
}
