package reega.data.exporter;

import reega.data.models.Data;

import java.io.File;
import java.util.List;

public class CsvExporter implements ReegaExporter {
    private final List<Data> data;

    protected CsvExporter(final List<Data> data) {
        this.data = data;
    }

    @Override
    public void export(final File file) {
        // TODO
    }
}
