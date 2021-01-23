package reega.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public final class PriceModel {
    private final int id;
    private final String name;
    private final Map<String, Double> prices;

    public PriceModel(final int id, final String name, final Map<String, Double> prices) {
        this.id = id;
        this.name = name;
        this.prices = prices;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Double> getPrices() {
        return this.prices;
    }

    public static final class PriceModelJsonSerialized {
        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("prices")
        public Map<String, Double> prices;

        public PriceModel getModel() {
            return new PriceModel(id, name, prices);
        }
    }

    @Override
    public String toString() {
        return "PriceModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prices=" + prices +
                '}';
    }
}
