package reega.data.models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class Contract {
	private final int id;
	private final int userID;
	private final String address;
	private final List<ServiceType> services;
	private final PriceModel priceModel;
	private final Date startDate;

	public int getId() {
		return this.id;
	}

	public int getUserID() {
		return this.userID;
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

	public Contract(final int id, final int userID, final String address, final List<String> services,
			final PriceModel priceModel, final Date startDate) {
		this.id = id;
		this.userID = userID;
		this.address = address;
		this.services = services.stream().map(s -> ServiceType.valueOf(s.toUpperCase())).collect(Collectors.toList());
		this.priceModel = priceModel;
		this.startDate = startDate;
	}

	public Contract(final int userID, final String address, final List<String> services, final PriceModel priceModel,
			final Date startDate) {
		this(0, userID, address, services, priceModel, startDate);
	}

	@Override
	public String toString() {
		return "Contract{" +
				"id=" + id +
				", userID=" + userID +
				", address='" + address + '\'' +
				", services=" + services +
				", priceModel=" + priceModel +
				", startDate=" + startDate +
				'}';
	}
}
