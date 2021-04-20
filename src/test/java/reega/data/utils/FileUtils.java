package reega.data.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public final class FileUtils {
    private FileUtils() {
    }

    /**
     * Get a file from the resources.
     *
     * @param fileName file name
     * @return a {@link File} gathered from the local resources
     * @throws URISyntaxException
     */
    public static File getFileFromResources(final String fileName) throws URISyntaxException {
        final ClassLoader classLoader = FileUtils.class.getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file " + fileName + " not found");
        } else {
            return new File(resource.toURI());
        }

    }

    /**
     * Get the file from the resources as string.
     *
     * @param fileName file name
     * @return a {@link String} containing all the data from the file
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String getFileFromResourcesAsString(final String fileName) throws IOException, URISyntaxException {
        final File f = FileUtils.getFileFromResources(fileName);
        return new String(Files.readAllBytes(f.toPath()));
    }
}
