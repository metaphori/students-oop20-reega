package reega.main;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reega.data.DataController;
import reega.data.DataControllerFactory;
import reega.data.remote.RemoteConnection;
import reega.generation.DataFiller;
import reega.generation.OnDemandDataFiller;

public class GenerationLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerationLauncher.class);

    public static void main(String[] args) {

        final String accessToken = System.getenv("AUTH_TOKEN");
        final RemoteConnection connection = new RemoteConnection();
        connection.overrideToken(accessToken);
        final DataController controller = DataControllerFactory.getDefaultDataController(connection);

        try {
            DataFiller generation = new OnDemandDataFiller(controller, controller.getAllContracts());
            generation.fill();
        } catch (IOException | SQLException e) {
            LOGGER.error("couldn't access DB");
        }

        System.exit(0);
    }

}
