package reega.data.utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import reega.data.ContractController;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;

public class ContractUtils {

    public static void insertContract(final ContractController controller, final String address, final String userFC,
            final long timestamp) throws IOException {
        final List<ServiceType> services = List.of(ServiceType.GAS, ServiceType.WATER, ServiceType.GARBAGE,
                ServiceType.ELECTRICITY);
        final NewContract newContract = new NewContract(address, services, userFC, new Date(timestamp));
        controller.addContract(newContract);
    }
}
