package reega.data.models;

import reega.data.models.gson.PriceModel;

import java.util.Map;

/**
 * This object describe a PriceModel. It contains the prices for all the services supplied by Reega
 */
public final class Prices {
    private final int id;
    private final String name;
    private final Map<String, Double> prices;

    public Prices(final int id, final String name, final Map<String, Double> prices) {
        this.id = id;
        this.name = name;
        this.prices = prices;
    }

    public Prices(PriceModel priceModel) {
        this(priceModel.id, priceModel.name, priceModel.prices);
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

    public PriceModel getJsonModel() {
        return new PriceModel(this);
    }

    @Override
    public String toString() {
        return this.getJsonModel().toString();
    }
}
