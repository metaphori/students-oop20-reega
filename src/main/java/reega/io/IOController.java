/**
 *
 */
package reega.io;

import java.io.File;

/**
 * Interface for handling saving data to disk.
 *
 */
public interface IOController {

    /**
     * Get the default directory of the application.
     *
     * @return the default directory of the application used for storing data
     */
    File getDefaultDirectory();

    /**
     * Get the absolute default directory path of the application.
     *
     * @return the default directory path of the application used for storing data
     * @see #getDefaultDirectory()
     */
    default String getDefaultDirectoryPath() {
        return this.getDefaultDirectory().getAbsolutePath();
    }
}
