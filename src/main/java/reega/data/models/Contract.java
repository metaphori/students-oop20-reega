package reega.data.models;

import com.google.gson.Gson;
import reega.data.models.gson.ContractModel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This object models a Contract. It contains informations about prices, services, and the contract itself
 */
public final class Contract {
    private final int id;
    private final String address;
    private final List<ServiceType> services;
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

    public Date getStartDate() {
        return this.startDate;
    }

    public Contract(final int id, final String address, final List<String> services, final Date startDate) {
        this.id = id;
        this.address = address;
        this.services = services.stream().map(s -> ServiceType.valueOf(s.toUpperCase())).collect(Collectors.toList());
        this.startDate = startDate;
    }

    public Contract(ContractModel contractModel) {
        this(contractModel.id,
                contractModel.address,
                contractModel.services,
                contractModel.startTime);
    }

    public ContractModel getJsonModel() {
        return new ContractModel(this);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this.getJsonModel());
    }
}
