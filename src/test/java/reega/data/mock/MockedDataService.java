package reega.data.mock;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.IOUtils;
import reega.data.models.Contract;
import reega.data.remote.models.DataModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MockedDataService {
    private final Map<Integer, Map<Long, Double>> dataValues = new HashMap<>();
    private final Contract defaultContract;

    // TODO implement methods to add and menage contracts
    public MockedDataService(Contract defaultContract) {
        this.defaultContract = defaultContract;
    }

    MockResponse contracts() throws IOException {
        final String body = getResponse("contracts.json");
        return new MockResponse().setResponseCode(200).setBody(body);
    }

    MockResponse fillData(RecordedRequest recordedRequest) {
        DataModel d = new Gson().fromJson(recordedRequest.getBody().getBuffer().readUtf8(), DataModel.class);
        assertNotNull(d);
        if (d.contractId == defaultContract.getId()) {
            this.dataValues.put(d.type, d.data);
        }
        return new MockResponse().setResponseCode(200);
    }

    MockResponse getLatestTimestamp(RecordedRequest recordedRequest) {
        HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        String type = url.queryParameter("type");
        String contract = url.queryParameter("contract_id");
        if (type == null || contract == null) {
            return new MockResponse().setResponseCode(404);
        }
        int typeId = Integer.parseInt(type);
        int contractId = Integer.parseInt(contract);
        if (contractId != defaultContract.getId()) {
            return new MockResponse().setResponseCode(400);
        }
        Map<Long, Double> val = dataValues.get(typeId);
        Optional<Long> latest = val.keySet().stream().reduce(Math::max);
        Date d = new Date(latest.orElse(0L));
        String resp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(d);
        return new MockResponse().setResponseCode(200).setBody("\"" + resp + "\"");
    }

    private String getResponse(String response) throws IOException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("mock-response/" + response);
        assert is != null;
        return IOUtils.toString(is, StandardCharsets.UTF_8.name());
    }
}
