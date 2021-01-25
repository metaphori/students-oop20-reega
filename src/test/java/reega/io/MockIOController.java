/**
 *
 */
package reega.io;

import java.io.File;
import java.io.IOException;

import org.junit.rules.TemporaryFolder;

/**
 * @author Marco
 *
 */
public class MockIOController implements IOController {

    private final TemporaryFolder tmpFolder = TemporaryFolder.builder().assureDeletion().build();

    private final File defaultDirectory;

    public MockIOController() throws IOException {
        this.tmpFolder.create();
        this.defaultDirectory = this.tmpFolder.newFolder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getDefaultDirectory() {
        return this.defaultDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultDirectoryPath() {
        return this.defaultDirectory.getAbsolutePath();
    }

}
