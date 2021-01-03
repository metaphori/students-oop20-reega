/**
 *
 */
package reega.io;

import java.io.File;

/**
 * @author Marco
 *
 */
public interface IOController {

    /**
     * Get the default directory of the application
     *
     * @return the default directory of the application used for storing data
     */
    File getDefaultDirectory();

    /**
     * Get the default directory path of the application
     *
     * @return the default directory path of the application used for storing data
     * @see #getDefaultDirectory()
     */
    String getDefaultDirectoryPath();

    /**
     * Get the file path of the token file
     *
     * @return the file path of the token
     */
    String getTokenFilePath();
}
