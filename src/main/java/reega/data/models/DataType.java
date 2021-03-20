package reega.data.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataType {
    ELECTRICITY(0, ServiceType.ELECTRICITY), GAS(1, ServiceType.GAS), WATER(2, ServiceType.WATER),
    PAPER(3, ServiceType.GARBAGE), GLASS(4, ServiceType.GARBAGE), PLASTIC(5, ServiceType.GARBAGE),
    MIXED(6, ServiceType.GARBAGE);

    private final ServiceType svcType;
    private final int id;

    private DataType(int id, ServiceType svcType) {
        this.id = id;
        this.svcType = svcType;
    }

    public static List<DataType> getDataTypesByService(ServiceType svcType) {
        return Arrays.stream(DataType.values()).filter(e -> e.getServiceType() == svcType).collect(Collectors.toList());
    }

    public ServiceType getServiceType() {
        return this.svcType;
    }

    public int getID() {
        return this.id;
    }
}
