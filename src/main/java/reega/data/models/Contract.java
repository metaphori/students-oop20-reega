package reega.data.models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This object models a Contract. It contains informations
 * about prices, services, and the contract itself
 */
public final class Contract {
	private final int id;
	private final String address;
	private final List<ServiceType> services;
	private final PriceModel priceModel;
	private final Date startDate;

	public int getId() {
		return this.id;
	}

	public String getAddress() {
		return this.address;
	}

	public List<ServiceType> getServices() {
		return this.services;
	}

	public PriceModel getPriceModel() {
		return this.priceModel;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Contract(final int id, final String address, final List<String> services,
			final PriceModel priceModel, final Date startDate) {
		this.id = id;
		this.address = address;
		this.services = services.stream().map(s -> ServiceType.valueOf(s.toUpperCase())).collect(Collectors.toList());
		this.priceModel = priceModel;
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "Contract{" +
				"id=" + id +
				", address='" + address + '\'' +
				", services=" + services +
				", priceModel=" + priceModel +
				", startDate=" + startDate +
				'}';
	}
}
