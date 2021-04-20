package reega.data.models;

import java.util.Map;

/**
 * Services supplied by Reega.
 */
public enum ServiceType {
    /**
     * Electricity type.
     */
    ELECTRICITY(0),
    /**
     * Gas type.
     */
    GAS(1),
    /**
     * Water type.
     */
    WATER(2),
    /**
     * Garbage type.
     */
    GARBAGE(3);

    private final Map<Integer, String> names = Map.of(0, "electricity", 1, "gas", 2, "water", 3, "garbage");
    private final int id;

    ServiceType(final int id) {
        this.id = id;
    }

    /**
     * Get the name.
     *
     * @return the name
     */
    public String getName() {
        return this.names.get(this.id);
    }

    /**
     * Get the ID.
     *
     * @return the ID
     */
    public int getID() {
        return this.id;
    }

    /**
     * Get the measurement unit of the specified <code>svcType</code>.
     *
     * @param svcType service type
     * @return a {@link String} representing the measurement unit used for the specified {@link ServiceType}
     */
    public static String getMeasurementUnit(final ServiceType svcType) {
        switch (svcType) {
            case ELECTRICITY:
                return "kWh";
            case GAS:
                return "m^3";
            case WATER:
                return "L";
            case GARBAGE:
                return "Kg";
            default:
                return null;
        }
    }
}
