package reega.data.models;

import reega.data.models.gson.ContractModel;

import java.util.Date;
import java.util.List;

public interface Contract {
    /**
     * @return the contract's ID
     */
    int getId();

    /**
     * @return the address related to this contract
     */
    String getAddress();

    /**
     * @return the list of provided services
     */
    List<ServiceType> getServices();

    /**
     * @return the contract's start date
     */
    Date getStartDate();

    /**
     * @return the JSON model
     */
    ContractModel getJsonModel();
}
