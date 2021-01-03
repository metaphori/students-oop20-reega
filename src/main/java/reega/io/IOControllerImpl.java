/**
 *
 */
package reega.io;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

/**
 * @author Marco
 *
 */
public class IOControllerImpl implements IOController {

    private static IOControllerImpl INSTANCE;

    public synchronized static IOControllerImpl getInstance() {
        if (IOControllerImpl.INSTANCE == null) {
            IOControllerImpl.INSTANCE = new IOControllerImpl();
        }
        return IOControllerImpl.INSTANCE;
    }

    /**
     * URI of the directory of the app
     */
    private static final String APP_DIRECTORY_URI;
    /**
     * URI of the token file
     */
    private static final String TOKEN_FILE_URI;

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

        TOKEN_FILE_URI = IOControllerImpl.APP_DIRECTORY_URI + File.separator + "token.reega";
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenFilePath() {
        return IOControllerImpl.TOKEN_FILE_URI;
    }

}
