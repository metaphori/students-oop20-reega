package reega.data.exporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.models.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CsvExporter implements ReegaExporter {
    private static final Logger logger = LoggerFactory.getLogger(CsvExporter.class);
    private final List<Data> data;

    protected CsvExporter(final List<Data> data) {
        this.data = Objects.requireNonNullElse(data, new ArrayList<>());
    }

    @Override
    public void export(final File file) throws IOException {
        logger.info("exporting data in csv format to " + file.getAbsolutePath());
        final FileOutputStream outputStream = new FileOutputStream(file);
        // writing header
        outputStream.write("timestamp,contract_id,type,value\n".getBytes(StandardCharsets.UTF_8));

        data.stream().collect(Collectors.groupingBy(Data::getContractID))
                .entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(contract -> contract.getValue().forEach(
                        val -> val.getData().entrySet().stream()
                                .sorted(Comparator.comparingLong(Map.Entry::getKey)).map(
                                        record -> csvRow(record.getKey(), contract.getKey(), val.getType().getID(), record.getValue())
                                ).forEach(k -> {
                                    try {
                                        outputStream.write(k.getBytes(StandardCharsets.UTF_8));
                                    } catch (IOException e) {
                                        throw new UncheckedIOException(e);
                                    }
                                })
                        )
                );
        outputStream.flush();
        outputStream.close();
        logger.info("export complete");
    }

    private String csvRow(Object... element) {
        return Arrays.stream(element).map(String::valueOf).collect(Collectors.joining(",")) + "\n";
    }
}
