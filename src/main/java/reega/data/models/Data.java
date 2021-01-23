package reega.data.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a container for the data to be pushed into the DB
 */
public final class Data {
	private final int contractID;
	private final ServiceType type;
	private final Map<Long, Double> data = new HashMap<>();

	public Data(int contractID, ServiceType serviceType) {
		this.contractID = contractID;
		this.type = serviceType;
	}

	public void addRecord(long timestamp, double value) {
		this.data.put(timestamp, value);
	}

	public void addRecords(Map<Long, Double> values) {
		this.data.putAll(values);
	}

	public int getContractID() {
		return contractID;
	}

	public ServiceType getType() {
		return type;
	}

	public Map<Long, Double> getData() {
		return data;
	}
}
