package reega.data;

import reega.data.models.PriceModel;
import reega.data.remote.models.NewContract;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TestClass {
    public static void main(String[] args) throws IOException, SQLException {
        DataController controller = DataControllerFactory.getRemotelDatabaseController();
        NewContract contract = new NewContract("funziona :)",
                2,
                "[\"garbage\", \"electricity\", \"gas\", \"water\"]",
                "EEEESSSSEEEEEH",
                new Date()
        );

        AuthControllerFactory.getRemoteAuthController().emailLogin("admin@reega.it", "AES_PASSWORD");
        //controller.addContract(contract);
        List<PriceModel> priceModels = controller.getPriceModels();
        priceModels.forEach(System.out::println);
    }
}
