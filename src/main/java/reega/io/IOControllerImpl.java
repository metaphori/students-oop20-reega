/**
 *
 */
package reega.io;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

/**
 *
 */
public final class IOControllerImpl implements IOController {

    private static IOControllerImpl instance;
    /**
     * URI of the directory of the app.
     */
    private static final String APP_DIRECTORY_URI;

    static {
        final String baseDir = System.getProperty("user.home");
        // For Windows, create a folder in the AppData\Local directory of the current
        // user, for the other systems create a folder in the /home/user directory
        if (SystemUtils.IS_OS_WINDOWS) {
            APP_DIRECTORY_URI = baseDir + File.separator + "AppData" + File.separator + "Local" + File.separator
                    + "Reega";
        } else {
            APP_DIRECTORY_URI = baseDir + File.separator + ".reega";
        }

        final File dir = new File(IOControllerImpl.APP_DIRECTORY_URI);
        if (!dir.exists()) {
            // Create the directory if it doesn't exist
            dir.mkdir();
        }
    }

    /**
     * Get the static instance of the {@link IOControllerImpl}.
     *
     * @return the static instance of the {@link IOControllerImpl}
     */
    public static synchronized IOControllerImpl getInstance() {
        if (IOControllerImpl.instance == null) {
            IOControllerImpl.instance = new IOControllerImpl();
        }
        return IOControllerImpl.instance;
    }

    private IOControllerImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getDefaultDirectory() {
        return new File(IOControllerImpl.APP_DIRECTORY_URI);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultDirectoryPath() {
        return IOControllerImpl.APP_DIRECTORY_URI;
    }

}
