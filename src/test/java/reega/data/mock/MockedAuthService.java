package reega.data.mock;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MockedAuthService {
    MockResponse addUser(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    MockResponse emailLogin(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    MockResponse fcLogin(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    MockResponse tokenLogin(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    MockResponse storeUserToken(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    MockResponse logout(RecordedRequest recordedRequest) {
        return new MockResponse().setResponseCode(500);
    }

    private String getResponse(String response) throws IOException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("mock-response/" + response);
        assert is != null;
        return IOUtils.toString(is, StandardCharsets.UTF_8.name());
    }
}
