package reega.data.remote.models;

import com.google.gson.annotations.SerializedName;
import reega.data.models.PriceModel;

import java.util.Date;
import java.util.List;

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
    @SerializedName("price_model")
    public PriceModel.PriceModelJsonSerialized priceModel;
}
