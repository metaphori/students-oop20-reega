package reega.data.models.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MonthlyReportModel {
    @SerializedName("month")
    public Date month;
    @SerializedName("average")
    public double average;
    @SerializedName("sum")
    public double sum;
    @SerializedName("type")
    public int type;
}
