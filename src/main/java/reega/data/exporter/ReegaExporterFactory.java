package reega.data.exporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UnknownFormatFlagsException;

import reega.data.models.Data;

public final class ReegaExporterFactory {
    private ReegaExporterFactory() {
    }

    /**
     * Export <code>data</code> to the file <code>file</code> with the specified <code>ExportFormat</code>.
     *
     * @param format format of the exported data
     * @param data   raw data
     * @param file   file path
     * @throws IOException
     */
    public static void export(final ExportFormat format, final List<Data> data, final String file) throws IOException {
        final File outputFile = new File(file);
        if (!outputFile.exists() && !outputFile.createNewFile()) {
            throw new IOException("Unable to access or create file " + file);
        }
        final List<Data> dataToBeExported = Objects.requireNonNullElse(data, new ArrayList<>());
        ReegaExporter exporter;
        switch (format) {
            case JSON:
                exporter = new JsonExporter(dataToBeExported);
                break;
            case CSV:
                exporter = new CsvExporter(dataToBeExported);
                break;
            default:
                throw new UnknownFormatFlagsException("Invalid ExportFormat!");
        }
        exporter.export(outputFile);
    }
}
