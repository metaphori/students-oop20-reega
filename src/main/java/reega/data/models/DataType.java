package reega.data.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum indicating the types of data supported by the Reega platform.
 *
 */
public enum DataType {
    /**
     * Electricity type, it corresponds to the {@link ServiceType#ELECTRICITY}.
     */
    ELECTRICITY(0, "electricity", ServiceType.ELECTRICITY),
    /**
     * Gas type, it corresponds to the {@link ServiceType#GAS}.
     */
    GAS(1, "gas", ServiceType.GAS),
    /**
     * Water type, it corresponds to the {@link ServiceType#WATER}.
     */
    WATER(2, "water", ServiceType.WATER),
    /**
     * Paper type, it is part of the {@link ServiceType#GARBAGE}.
     */
    PAPER(3, "paper", ServiceType.GARBAGE),
    /**
     * Glass type, it is part of the {@link ServiceType#GARBAGE}.
     */
    GLASS(4, "glass", ServiceType.GARBAGE),
    /**
     * Plastic type, it is part of the {@link ServiceType#GARBAGE}.
     */
    PLASTIC(5, "plastic", ServiceType.GARBAGE),
    /**
     * Mixed type, it is part of the {@link ServiceType#GARBAGE}.
     */
    MIXED(6, "mixed", ServiceType.GARBAGE);

    private final ServiceType svcType;
    private final int id;
    private final String name;

    DataType(final int id, final String name, final ServiceType svcType) {
        this.id = id;
        this.svcType = svcType;
        this.name = name;
    }

    /**
     * Get the {@link DataType} corresponding to the <code>id</code>.
     *
     * @param id id that can correspond to a {@link DataType}
     * @return {@link DataType} corresponding to the <code>id</code>, or null if it doesn't correspond to any
     *         {@link DataType}
     */
    public static DataType fromId(final int id) {
        for (final DataType t : DataType.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

    /**
     * Get all the data types corresponding to the <code>svcType</code> specified.
     *
     * @param svcType {@link ServiceType} that needs to get all its {@link DataType}
     * @return a {@link List} of {@link DataType}
     */
    public static List<DataType> getDataTypesByService(final ServiceType svcType) {
        return Arrays.stream(DataType.values()).filter(e -> e.getServiceType() == svcType).collect(Collectors.toList());
    }

    /**
     * Get the {@link ServiceType} corresponding to this {@link DataType}.
     *
     * @return the {@link ServiceType} corresponding to this {@link DataType}
     */
    public ServiceType getServiceType() {
        return this.svcType;
    }

    /**
     * Get the id of this {@link DataType}.
     *
     * @return the id of this {@link DataType}
     */
    public int getID() {
        return this.id;
    }

    /**
     * Get the name of this {@link DataType}.
     *
     * @return the name of this {@link DataType}
     */
    public String getName() {
        return this.name;
    }
}
