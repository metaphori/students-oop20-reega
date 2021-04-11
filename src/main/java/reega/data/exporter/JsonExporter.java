package reega.data.exporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.models.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class JsonExporter implements ReegaExporter {
    private static final Logger logger = LoggerFactory.getLogger(JsonExporter.class);
    private final List<Data> data;

    protected JsonExporter(final List<Data> data) {
        this.data = data;
    }

    @Override
    public void export(final File file) throws IOException {
        logger.info("exporting data in json format to " + file.getAbsolutePath());
        final FileOutputStream outputStream = new FileOutputStream(file);
        var metrics = data.stream()
                .collect(Collectors.groupingBy(Data::getContractID))
                .entrySet().stream().map(contracts -> new ContractEntry(
                        contracts.getKey(),
                        contracts.getValue().stream()
                                .map(values -> new DataEntry(values.getType().getName(), values.getData()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String output = gson.toJson(metrics);
        outputStream.write(output.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        logger.info("export complete");
    }

    private static class DataEntry {
        @SerializedName("type")
        final String type;
        @SerializedName("values")
        final Map<String, Double> values;

        public DataEntry(final String type, final Map<Long, Double> values) {
            this.type = type;
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            format.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
            this.values = values.entrySet().stream().collect(Collectors.toMap(
                    e -> format.format(new Date(e.getKey())),
                    Map.Entry::getValue
            ));
        }
    }

    private static class ContractEntry {
        @SerializedName("contract_id")
        final int contractId;
        @SerializedName("values")
        final List<DataEntry> values;

        public ContractEntry(final int contractId, final List<DataEntry> values) {
            this.contractId = contractId;
            this.values = values;
        }
    }
}
