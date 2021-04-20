package reega.data.models;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import reega.data.models.gson.DataModel;

/**
 * This is a container for the data to be pushed into the DB.
 */
public final class Data {
    private final int contractID;
    private final DataType type;
    private final Map<Long, Double> dataValuesByTimestamp;

    public Data(final DataModel model) {
        this.contractID = model.contractId;
        this.dataValuesByTimestamp = model.data;
        this.type = DataType.fromId(model.type);
    }

    public Data(final int contractID, final DataType dataType) {
        this(contractID, dataType, new HashMap<>());
    }

    public Data(final int contractID, final DataType dataType, final Map<Long, Double> data) {
        this.contractID = contractID;
        this.type = dataType;
        this.dataValuesByTimestamp = data;
    }

    /**
     * Get the JSON model of this class
     *
     * @return the JSON model of this class
     */
    public DataModel getJsonModel() {
        return new DataModel(this);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this.getJsonModel());
    }

    /**
     * Add a record to current records.
     *
     * @param timestamp timestamp of the record
     * @param value     value of the record
     */
    public void addRecord(final long timestamp, final double value) {
        this.dataValuesByTimestamp.put(timestamp, value);
    }

    /**
     * Add a {@link Map} of records to the current record.
     *
     * @param values values to be added to the current records
     */
    public void addRecords(final Map<Long, Double> values) {
        this.dataValuesByTimestamp.putAll(values);
    }

    /**
     * Get the contract ID that owns this data object.
     *
     * @return the contract ID
     */
    public int getContractID() {
        return this.contractID;
    }

    /**
     * Get the {@link DataType} of this data object.
     *
     * @return a {@link DataType} of this data object
     */
    public DataType getType() {
        return this.type;
    }

    /**
     * Get the map of records.
     *
     * @return the map of records
     */
    public Map<Long, Double> getData() {
        return this.dataValuesByTimestamp;
    }
}
