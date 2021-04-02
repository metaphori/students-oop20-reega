package reega.data.models;

import com.google.gson.Gson;
import reega.data.models.gson.DataModel;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a container for the data to be pushed into the DB
 */
public final class Data {
    private final int contractID;
    private final DataType type;
    private final Map<Long, Double> data;

    public Data(final DataModel model) {
        this.contractID = model.contractId;
        this.data = model.data;
        this.type = DataType.fromId(model.type);
    }

    public Data(final int contractID, final DataType dataType) {
        this(contractID, dataType, new HashMap<>());
    }

    public Data(final int contractID, final DataType dataType, Map<Long, Double> data) {
        this.contractID = contractID;
        this.type = dataType;
        this.data = data;
    }

    public DataModel getJsonModel() {
        return new DataModel(this);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this.getJsonModel());
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
