package reega.data.exporter;

import reega.data.models.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UnknownFormatFlagsException;

public class ReegaExporterFactory {
    public static void export(ExportFormat format, List<Data> data, String file) throws IOException {
        data = Objects.requireNonNullElse(data, new ArrayList<>());
        File outputFile = new File(file);
        if (!outputFile.exists() && !outputFile.createNewFile()) {
            throw new IOException("Unable to access or create file " + file);
        }
        ReegaExporter exporter;
        switch (format) {
            case JSON:
                exporter = new JsonExporter(data);
                break;
            case CSV:
                exporter = new CsvExporter(data);
                break;
            default:
                throw new UnknownFormatFlagsException("Invalid ExportFormat!");
        }
        exporter.export(outputFile);
    }
}
