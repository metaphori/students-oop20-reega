package reega.data.models;

import com.google.gson.Gson;
import reega.data.models.gson.ContractModel;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This object models a Contract. It contains info about services and the contract itself
 */
public final class BaseContract implements Contract {
    private final int id;
    private final String address;
    private final List<ServiceType> services;
    private final Date startDate;

    public BaseContract(final int id, final String address, final List<String> services, final Date startDate) {
        this.id = id;
        this.address = address;
        this.services = services.stream()
                .map(s -> ServiceType.valueOf(s.toUpperCase(Locale.US)))
                .collect(Collectors.toList());
        this.startDate = startDate;
    }

    public BaseContract(final ContractModel contractModel) {
        this(contractModel.id, contractModel.address, contractModel.services, contractModel.startTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAddress() {
        return this.address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceType> getServices() {
        return this.services;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContractModel getJsonModel() {
        return new ContractModel(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new Gson().toJson(this.getJsonModel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BaseContract contract = (BaseContract) o;
        return this.id == contract.id && this.address.equals(contract.address)
                && this.services.equals(contract.services) && this.startDate.equals(contract.startDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.address, this.services, this.startDate);
    }
}
