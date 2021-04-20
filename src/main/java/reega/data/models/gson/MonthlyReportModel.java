package reega.data.models.gson;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

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
