package reega.data.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class FileUtils {
    public static File getFileFromResources(String fileName) throws URISyntaxException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file " + fileName + " not found");
        } else {
            return new File(resource.toURI());
        }

    }

    public static String getFileFromResourcesAsString(String fileName) throws IOException, URISyntaxException {
        final File f = getFileFromResources(fileName);
        return new String(Files.readAllBytes(f.toPath()));
    }
}
