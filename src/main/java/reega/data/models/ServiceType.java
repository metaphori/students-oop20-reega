package reega.data.models;

import java.util.Map;

/**
 * Services supplied by Reega
 */
public enum ServiceType {
    ELECTRICITY(0),
    GAS(1),
    WATER(2),
    GARBAGE(3);

    private final Map<Integer, String> names = Map.of(
            0, "electricity",
            1, "gas",
            2, "water",
            3, "garbage"
    );
    private final int id;

    ServiceType(final int id) {
        this.id = id;
    }

    public String getName() {
        return this.names.get(id);
    }

    public int getID() {
        return this.id;
    }
}
