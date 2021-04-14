package reega.data.utils;

import reega.data.ContractController;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ContractUtils {

    public static void insertContract(ContractController controller, String address, String userFC, long timestamp) throws IOException {
        List<ServiceType> services = List.of(
                ServiceType.GAS,
                ServiceType.WATER,
                ServiceType.GARBAGE,
                ServiceType.ELECTRICITY
        );
        NewContract newContract = new NewContract(address, services, userFC, new Date(timestamp));
        controller.addContract(newContract);
    }
}
