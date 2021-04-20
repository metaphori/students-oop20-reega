package reega.data.models;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import reega.data.models.gson.ContractModel;

/**
 * This object models a Contract. It contains informations about prices, services, and the contract itself
 */
public final class Contract {
    private final int id;
    private final String address;
    private final List<ServiceType> services;
    private final Date startDate;

    public Contract(final int id, final String address, final List<String> services, final Date startDate) {
        this.id = id;
        this.address = address;
        this.services = services.stream()
                .map(s -> ServiceType.valueOf(s.toUpperCase(Locale.US)))
                .collect(Collectors.toList());
        this.startDate = startDate;
    }

    public Contract(final ContractModel contractModel) {
        this(contractModel.id, contractModel.address, contractModel.services, contractModel.startTime);
    }

    public int getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public List<ServiceType> getServices() {
        return this.services;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public ContractModel getJsonModel() {
        return new ContractModel(this);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this.getJsonModel());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Contract contract = (Contract) o;
        return this.id == contract.id && this.address.equals(contract.address)
                && this.services.equals(contract.services) && this.startDate.equals(contract.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.address, this.services, this.startDate);
    }
}
