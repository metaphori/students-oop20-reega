package reega.data.models;

/**
 * Specific types of Garbage
 */
public enum GarbageType {
    PAPER("paper", 0),
    GLASS("glass", 1),
    PLASTIC("plastic", 2),
    MIXED("mixed", 3);

    private final String name;
    private final int id;

    GarbageType(String name, int id) {
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
