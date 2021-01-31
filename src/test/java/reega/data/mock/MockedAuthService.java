package reega.data.mock;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import reega.data.remote.models.LoginResponse;
import reega.data.remote.models.NewUserBody;
import reega.data.remote.models.UserAuthToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MockedAuthService {
    private Map<Integer, NewUserBody> users = new HashMap<>();
    private Map<Integer, UserAuthToken> tokens = new HashMap<>();
    private int userID = 0;

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void clean() {
        this.users = new HashMap<>();
        this.tokens = new HashMap<>();
        this.userID = 0;
    }

    MockResponse addUser(RecordedRequest recordedRequest) {
        NewUserBody user = new Gson().fromJson(recordedRequest.getBody().readUtf8(), NewUserBody.class);
        if (user == null) {
            return new MockResponse().setResponseCode(400);
        }
        users.put(userID, user);
        return new MockResponse().setResponseCode(200);
    }

    MockResponse emailLogin(RecordedRequest recordedRequest) {
        HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        String email = url.queryParameter("email");
        String password = url.queryParameter("password");
        if (email == null || password == null) {
            return new MockResponse().setResponseCode(404);
        }
        return genericLogin("email", email, password);
    }

    MockResponse fcLogin(RecordedRequest recordedRequest) {
        HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        String fc = url.queryParameter("fc");
        String password = url.queryParameter("password");
        if (fc == null || password == null) {
            return new MockResponse().setResponseCode(404);
        }
        return genericLogin("fc", fc, password);
    }

    MockResponse storeUserToken(RecordedRequest recordedRequest) {
        UserAuthToken userAuthToken = new Gson().fromJson(recordedRequest.getBody().readUtf8(), UserAuthToken.class);
        assertNotNull(userAuthToken);
        tokens.put(userID, userAuthToken);
        return new MockResponse().setResponseCode(200);
    }

    MockResponse tokenLogin(RecordedRequest recordedRequest) {
        HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        String selector = url.queryParameter("selector");
        String validator = url.queryParameter("validator");
        if (selector == null || validator == null) {
            return new MockResponse().setResponseCode(404);
        }
        UserAuthToken uat = new UserAuthToken(selector, validator);
        if (!tokens.containsKey(userID) || !tokens.get(userID).equals(uat) || !users.containsKey(userID)) {
            return new MockResponse().setResponseCode(500);
        }
        LoginResponse user = getLoginResponse(users.get(userID));
        return new MockResponse().setResponseCode(200).setBody(new Gson().toJson(user));
    }

    MockResponse logout() {
        tokens.remove(userID);
        return new MockResponse().setResponseCode(500);
    }

    private MockResponse genericLogin(String type, String value, String psk) {
        List<NewUserBody> usr;
        switch (type) {
            case "email":
                usr = users.values().stream().filter(u -> u.email.equals(value)).collect(Collectors.toList());
                break;
            case "fc":
                usr = users.values().stream().filter(u -> u.fiscalCode.equals(value)).collect(Collectors.toList());
                break;
            default:
                return new MockResponse().setResponseCode(500);
        }
        if (usr.size() < 1 || !BCrypt.checkpw(psk, users.get(0).passwordHash)) {
            return new MockResponse().setResponseCode(500);
        }
        LoginResponse user = getLoginResponse(usr.get(0));
        return new MockResponse().setResponseCode(200).setBody(new Gson().toJson(user));
    }

    private LoginResponse getLoginResponse(NewUserBody user) {
        return new LoginResponse(
                1,
                user.name,
                user.surname,
                user.email,
                user.fiscalCode,
                user.role,
                "ABC_123");
    }
}
