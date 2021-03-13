package reega.data.mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import reega.data.models.Contract;
import reega.data.remote.models.DataModel;

public class MockedDataService {
    private final Map<Integer, Map<Long, Double>> dataValues = new HashMap<>();
    private final Contract defaultContract;

    // TODO implement methods to add and menage contracts
    public MockedDataService(final Contract defaultContract) {
        this.defaultContract = defaultContract;
    }

    MockResponse contracts() throws IOException {
        final String body = this.getResponse("contracts.json");
        return new MockResponse().setResponseCode(200).setBody(body);
    }

    MockResponse fillData(final RecordedRequest recordedRequest) {
        final DataModel d = new Gson().fromJson(recordedRequest.getBody().readUtf8(), DataModel.class);
        assertNotNull(d);
        if (d.contractId == this.defaultContract.getId()) {
            this.dataValues.put(d.type, d.data);
        }
        return new MockResponse().setResponseCode(200);
    }

    MockResponse getLatestTimestamp(final RecordedRequest recordedRequest) {
        final HttpUrl url = recordedRequest.getRequestUrl();
        assertNotNull(url);
        final String type = url.queryParameter("type");
        final String contract = url.queryParameter("contract_id");
        if (type == null || contract == null) {
            return new MockResponse().setResponseCode(404);
        }
        final int typeId = Integer.parseInt(type);
        final int contractId = Integer.parseInt(contract);
        if (contractId != this.defaultContract.getId()) {
            return new MockResponse().setResponseCode(400);
        }
        final Map<Long, Double> val = this.dataValues.get(typeId);
        final Optional<Long> latest = val.keySet().stream().reduce(Math::max);
        final Date d = new Date(latest.orElse(0L));
        final String resp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(d);
        return new MockResponse().setResponseCode(200).setBody("\"" + resp + "\"");
    }

    private String getResponse(final String response) throws IOException {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("mock-response/" + response);
        assert is != null;
        return IOUtils.toString(is, StandardCharsets.UTF_8.name());
    }
}
