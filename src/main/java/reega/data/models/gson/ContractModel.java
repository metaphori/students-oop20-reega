package reega.data.models.gson;

import com.google.gson.annotations.SerializedName;
import reega.data.models.Contract;
import reega.data.models.ServiceType;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API data model
 */
public class ContractModel {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_id")
    public Integer userID;
    @SerializedName("address")
    public String address;
    @SerializedName("services")
    public List<String> services;
    @SerializedName("start_time")
    public Date startTime;

    public ContractModel(Contract contract){
        this.id = contract.getId();
        this.address = contract.getAddress();
        this.services = contract.getServices().stream().map(ServiceType::getName).collect(Collectors.toList());
        this.startTime = contract.getStartDate();
    }
}
