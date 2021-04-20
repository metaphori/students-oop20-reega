package reega.data.models.gson;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

import reega.data.models.Data;

/**
 * API data model
 */
public class DataModel {
    @SerializedName("type")
    public Integer type;
    @SerializedName("contract_id")
    public Integer contractId;
    @SerializedName("data")
    public Map<Long, Double> data;

    public DataModel(final Data data) {
        this.type = data.getType().getID();
        this.contractId = data.getContractID();
        this.data = data.getData();
    }
}
