package reega.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.remote.RemoteConnection;
import reega.generation.DataFiller;
import reega.generation.OnDemandDataFiller;

import java.io.IOException;

public final class GenerationLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerationLauncher.class);

    private GenerationLauncher() {
    }

    public static void main(final String[] args) {

        final String accessToken = System.getenv("AUTH_TOKEN");
        final RemoteConnection connection = new RemoteConnection();
        connection.overrideToken(accessToken);

        try {
            final DataFiller generation = new OnDemandDataFiller();
            generation.fill();
        } catch (final IOException e) {
            GenerationLauncher.LOGGER.error("couldn't access DB");
        }

        System.exit(0);
    }

}
