package reega.data;

import java.io.IOException;
import java.sql.SQLException;

public class TestClass {
    public static void main(String[] args) throws IOException, SQLException {
        AuthControllerFactory.getRemoteAuthController().emailLogin("admin@reega.it", "AES_PASSWORD");
        DataController controller = DataControllerFactory.getRemotelDatabaseController();

        controller.removeContract(10);
    }
}
