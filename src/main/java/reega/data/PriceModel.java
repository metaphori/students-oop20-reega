package reega.data;

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
}
