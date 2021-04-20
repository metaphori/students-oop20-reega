/**
 *
 */
package reega.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import reega.data.models.UserAuth;

/**
 *
 */
class TokenIOControllerImpl implements TokenIOController {

    private final IOController ioController;

    TokenIOControllerImpl(final IOController ioController) {
        this.ioController = ioController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getDefaultDirectory() {
        return this.ioController.getDefaultDirectory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultDirectoryPath() {
        return this.ioController.getDefaultDirectoryPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenFilePath() {
        return this.getDefaultDirectoryPath() + File.separator + "token.reega";
    }

    /**
     * Get the token file.
     *
     * @return the token file
     */
    private File getTokenFile() {
        return new File(this.getTokenFilePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeUserAuthentication(final UserAuth userAuth) throws IOException {
        final File tokenFile = this.getTokenFile();
        if (tokenFile.exists()) {
            return;
        }

        // Create the file if it doesn't exist
        tokenFile.createNewFile();

        try (FileOutputStream stream = new FileOutputStream(tokenFile);
                ObjectOutputStream oos = new ObjectOutputStream(stream)) {
            oos.writeObject(userAuth);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserAuthentication() throws IOException {
        final File tokenFile = this.getTokenFile();
        if (!tokenFile.exists()) {
            return;
        }

        Files.delete(Path.of(tokenFile.getAbsolutePath()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserAuth> readUserAuthentication() throws IOException {
        final File tokenFile = this.getTokenFile();
        if (!tokenFile.exists()) {
            return Optional.empty();
        }

        final UserAuth userAuth;
        try (FileInputStream stream = new FileInputStream(tokenFile);
                ObjectInputStream oos = new ObjectInputStream(stream)) {
            userAuth = (UserAuth) oos.readObject();
        } catch (final ClassNotFoundException e) {
            return Optional.empty();
        }

        return Optional.of(userAuth);
    }

}
