package reega.data.exporter;

import java.io.File;
import java.io.IOException;

public interface ReegaExporter {

    /**
     * Export data to the given file instance
     *
     * @param file to write the output on
     * @throws IOException
     */
    void export(final File file) throws IOException;
}
