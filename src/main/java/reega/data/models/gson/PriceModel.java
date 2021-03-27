package reega.data.models.gson;

import com.google.gson.annotations.SerializedName;
import reega.data.models.Prices;

import java.util.Map;

public class PriceModel {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("prices")
    public Map<String, Double> prices;

    public PriceModel(Prices prices) {
        this.id = prices.getId();
        this.prices = prices.getPrices();
        this.name = prices.getName();
    }
}
