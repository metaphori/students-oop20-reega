package reega.data.models;

/**
 * Services supplied by Reega
 */
public enum ServiceType {
    ELECTRICITY("electricity", 0),
    GAS("gas", 1),
    WATER("water", 2),
    GARBAGE("garbage", 3);

    private final String name;
    private final int id;

    ServiceType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }
}
