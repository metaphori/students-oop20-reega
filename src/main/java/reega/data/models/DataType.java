package reega.data.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataType {
    ELECTRICITY(0, "electricity", ServiceType.ELECTRICITY),
    GAS(1, "gas", ServiceType.GAS),
    WATER(2, "water", ServiceType.WATER),
    PAPER(3, "paper", ServiceType.GARBAGE),
    GLASS(4, "glass", ServiceType.GARBAGE),
    PLASTIC(5, "plastic", ServiceType.GARBAGE),
    MIXED(6, "mixed", ServiceType.GARBAGE);

    private final ServiceType svcType;
    private final int id;
    private final String name;

    DataType(int id, String name, ServiceType svcType) {
        this.id = id;
        this.svcType = svcType;
        this.name = name;
    }

    public static DataType fromId(int id) {
        for (DataType t : values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
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

    public String getName() {
        return this.name;
    }
}
