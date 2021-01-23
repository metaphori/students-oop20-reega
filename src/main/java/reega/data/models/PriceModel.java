package reega.data.models;

import java.util.Map;

/**
 * This object describe a PriceModel. It contains the prices for
 * all the services supplied by Reega
 */
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

    @Override
    public String toString() {
        return "PriceModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prices=" + prices +
                '}';
    }
}
