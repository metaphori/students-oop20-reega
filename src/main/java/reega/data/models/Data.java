package reega.data.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a container for the data to be pushed into the DB
 */
public final class Data {
    private final int contractID;
    private final DataType type;
    private final Map<Long, Double> data = new HashMap<>();

    public Data(final int contractID, final DataType dataType) {
        this.contractID = contractID;
        this.type = dataType;
    }

    public void addRecord(final long timestamp, final double value) {
        this.data.put(timestamp, value);
    }

    public void addRecords(final Map<Long, Double> values) {
        this.data.putAll(values);
    }

    public int getContractID() {
        return this.contractID;
    }

    public DataType getType() {
        return this.type;
    }

    public Map<Long, Double> getData() {
        return this.data;
    }
}
