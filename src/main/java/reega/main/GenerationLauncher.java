package reega.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.remote.RemoteConnection;
import reega.generation.DataFiller;
import reega.generation.OnDemandDataFiller;

import java.io.IOException;

public class GenerationLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerationLauncher.class);

    public static void main(String[] args) {

        final String accessToken = System.getenv("AUTH_TOKEN");
        final RemoteConnection connection = new RemoteConnection();
        connection.overrideToken(accessToken);

        try {
            DataFiller generation = new OnDemandDataFiller();
            generation.fill();
        } catch (IOException e) {
            LOGGER.error("couldn't access DB");
        }

        System.exit(0);
    }

}
