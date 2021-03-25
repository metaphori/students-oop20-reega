package reega.data.models.gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

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

    public NewContract(final String address, final int priceModelId, final String services, final String userFiscalCode,
            final Date startTime) {
        this.address = address;
        this.priceModelId = priceModelId;
        this.services = services;
        this.userFiscalCode = userFiscalCode;
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        this.startTime = format.format(startTime);
    }
}
