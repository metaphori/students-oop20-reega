package reega.data.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.data.models.Data;
import reega.main.Settings;

public class CsvExporter implements ReegaExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvExporter.class);
    private final List<Data> data;

    protected CsvExporter(final List<Data> data) {
        this.data = Objects.requireNonNullElse(data, new ArrayList<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void export(final File file) throws IOException {
        CsvExporter.LOGGER.info("exporting data in csv format to " + file.getAbsolutePath());
        final FileOutputStream outputStream = new FileOutputStream(file);
        // writing header
        outputStream.write("timestamp,contract_id,type,value\n".getBytes(StandardCharsets.UTF_8));

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        format.setTimeZone(TimeZone.getTimeZone(Settings.CLIENT_TIMEZONE));
        this.data.stream()
                .collect(Collectors.groupingBy(Data::getContractID))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(contract -> contract.getValue()
                        .forEach(val -> val.getData()
                                .entrySet()
                                .stream()
                                .sorted(Comparator.comparingLong(Map.Entry::getKey))
                                .map(record -> this.csvRow(format.format(new Date(record.getKey())), contract.getKey(),
                                        val.getType().getName(), record.getValue()))
                                .forEach(k -> {
                                    try {
                                        outputStream.write(k.getBytes(StandardCharsets.UTF_8));
                                    } catch (final IOException e) {
                                        throw new UncheckedIOException(e);
                                    }
                                })));
        outputStream.flush();
        outputStream.close();
        CsvExporter.LOGGER.info("export complete");
    }

    private String csvRow(final Object... element) {
        return Arrays.stream(element).map(String::valueOf).collect(Collectors.joining(",")) + "\n";
    }
}
