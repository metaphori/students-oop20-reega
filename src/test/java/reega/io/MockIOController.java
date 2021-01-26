/**
 *
 */
package reega.io;

import java.io.File;
import java.io.IOException;

/**
 * @author Marco
 *
 */
public class MockIOController implements IOController {

    private final File defaultDirectoryFile;

    public MockIOController() throws IOException {
        // Get the temporary directory based on the OS
        final String tmpDirPath = System.getProperty("java.io.tmpdir");
        this.defaultDirectoryFile = new File(tmpDirPath + File.separator + "reega");
        // Create the folder
        this.defaultDirectoryFile.mkdir();
        // Make it delete itself when the JVM shuts down
        this.defaultDirectoryFile.deleteOnExit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getDefaultDirectory() {
        return this.defaultDirectoryFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultDirectoryPath() {
        return this.defaultDirectoryFile.getAbsolutePath();
    }

}
