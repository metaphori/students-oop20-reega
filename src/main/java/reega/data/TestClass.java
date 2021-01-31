package reega.data;

import reega.data.models.PriceModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestClass {
    public static void main(String[] args) throws IOException, SQLException {
        AuthControllerFactory.getRemoteAuthController().emailLogin("admin@reega.it", "AES_PASSWORD");
        DataController controller = DataControllerFactory.getRemotelDatabaseController();

        PriceModel pm = new PriceModel(0, "test from java", Map.of("water", 3.3));
        controller.addPriceModel(pm);

        List<PriceModel> priceModels = controller.getPriceModels();
        priceModels.forEach(System.out::println);
        Optional<PriceModel> first = priceModels.stream().filter(p -> p.getName().equals(pm.getName())).findFirst();
        if (first.isPresent()) {
            controller.removePriceModel(first.get().getId());
        }

        priceModels = controller.getPriceModels();
        priceModels.forEach(System.out::println);
    }
}
