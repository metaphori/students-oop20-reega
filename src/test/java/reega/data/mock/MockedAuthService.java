package reega.data.mock;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import reega.data.models.gson.LoginResponse;
import reega.data.models.gson.NewUserBody;
import reega.data.models.gson.UserAuthToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MockedAuthService {
    private Map<Integer, NewUserBody> users = new HashMap<>();
    private Map<Integer, UserAuthToken> tokens = new HashMap<>();
    private int userID = 0;

    public void setUserID(final int userID) {
        this.userID = userID;
    }

    public void clean() {
        this.users = new HashMap<>();
        this.tokens = new HashMap<>();
        this.userID = 0;
    }

    MockResponse addUser(final RecordedRequest recordedRequest) {
        final NewUserBody user = new Gson().fromJson(recordedRequest.getBody().readUtf8(), NewUserBody.class);
        if (user == null) {
            return new MockResponse().setResponseCode(400);
        }
        this.users.put(this.userID, user);
        return new MockResponse().setResponseCode(200);
    }

    MockResponse removeUser(RecordedRequest recordedRequest) {
        HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        String fc = url.queryParameter("fc");
        if (fc == null) {
            return new MockResponse().setResponseCode(404);
        }
        Optional<Integer> first = users.keySet().stream().filter(k -> users.get(k).fiscalCode.equals(fc)).findFirst();
        if (first.isPresent()) {
            users.remove(first.get());
            tokens.remove(first.get());
        }
        return new MockResponse().setResponseCode(200);
    }

    MockResponse emailLogin(final RecordedRequest recordedRequest) {
        final HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        final String email = url.queryParameter("email");
        final String password = url.queryParameter("password");
        if (email == null || password == null) {
            return new MockResponse().setResponseCode(404);
        }
        return this.genericLogin("email", email, password);
    }

    MockResponse fcLogin(final RecordedRequest recordedRequest) {
        final HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        final String fc = url.queryParameter("fc");
        final String password = url.queryParameter("password");
        if (fc == null || password == null) {
            return new MockResponse().setResponseCode(404);
        }
        return this.genericLogin("fc", fc, password);
    }

    MockResponse storeUserToken(final RecordedRequest recordedRequest) {
        final UserAuthToken userAuthToken = new Gson().fromJson(recordedRequest.getBody().readUtf8(),
                UserAuthToken.class);
        assertNotNull(userAuthToken);
        this.tokens.put(this.userID, userAuthToken);
        return new MockResponse().setResponseCode(200);
    }

    MockResponse tokenLogin(final RecordedRequest recordedRequest) {
        final HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        final String selector = url.queryParameter("selector");
        final String validator = url.queryParameter("validator");
        if (selector == null || validator == null) {
            return new MockResponse().setResponseCode(404);
        }
        final UserAuthToken uat = new UserAuthToken(selector, validator);
        if (!this.tokens.containsKey(this.userID) || !this.tokens.get(this.userID).equals(uat)
                || !this.users.containsKey(this.userID)) {
            return new MockResponse().setResponseCode(500);
        }
        final LoginResponse user = this.getLoginResponse(this.users.get(this.userID));
        return new MockResponse().setResponseCode(200).setBody(new Gson().toJson(user));
    }

    MockResponse logout() {
        this.tokens.remove(this.userID);
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse genericLogin(final String type, final String value, final String psk) {
        List<NewUserBody> usr;
        switch (type) {
            case "email":
                usr = this.users.values().stream().filter(u -> u.email.equals(value)).collect(Collectors.toList());
                break;
            case "fc":
                usr = this.users.values().stream().filter(u -> u.fiscalCode.equals(value)).collect(Collectors.toList());
                break;
            default:
                return new MockResponse().setResponseCode(500);
        }
        if (usr.size() < 1 || !BCrypt.checkpw(psk, usr.get(0).passwordHash)) {
            return new MockResponse().setResponseCode(500);
        }
        final LoginResponse user = this.getLoginResponse(usr.get(0));
        return new MockResponse().setResponseCode(200).setBody(new Gson().toJson(user));
    }

    private LoginResponse getLoginResponse(final NewUserBody user) {
        return new LoginResponse(1, user.name, user.surname, user.email, user.fiscalCode, user.role, "ABC_123");
    }
}
