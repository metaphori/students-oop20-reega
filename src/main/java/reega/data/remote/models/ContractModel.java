package reega.data.remote.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * API data model
 */
public class ContractModel {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_id")
    public Integer userID;
    @SerializedName("address")
    public String address;
    @SerializedName("services")
    public List<String> services;
    @SerializedName("start_time")
    public Date startTime;
    @SerializedName("price_model")
    public PriceModel priceModel;

    public static class PriceModel {
        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("prices")
        public Map<String, Double> prices;

        public reega.data.models.PriceModel getPriceModel() {
            return new reega.data.models.PriceModel(this.id, this.name, this.prices);
        }
    }
}
