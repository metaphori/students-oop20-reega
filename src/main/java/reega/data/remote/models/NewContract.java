package reega.data.remote.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewContract {
    @SerializedName("address")
    public String address;
    @SerializedName("price_model_id")
    public int priceModelId;
    @SerializedName("services")
    public String services;
    @SerializedName("user_fiscal_code")
    public String userFiscalCode;
    @SerializedName("start_time")
    public String startTime;

    public NewContract(String address, int priceModelId, String services, String userFiscalCode, Date startTime) {
        this.address = address;
        this.priceModelId = priceModelId;
        this.services = services;
        this.userFiscalCode = userFiscalCode;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        this.startTime = format.format(startTime);
    }
}
