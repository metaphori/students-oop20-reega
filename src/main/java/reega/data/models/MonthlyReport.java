package reega.data.models;

import com.google.gson.Gson;
import reega.main.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class MonthlyReport {
    private final String month;
    private final Map<DataType, Report> reports;

    public MonthlyReport(final long date, final Map<DataType, Report> reports) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        format.setTimeZone(TimeZone.getTimeZone(Settings.CLIENT_TIMEZONE));
        this.month = format.format(new Date(date));

        this.reports = reports;
    }

    public String getMonth() {
        return month;
    }

    public Map<DataType, Report> getReports() {
        return reports;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class Report {
        private final double sum;
        private final double avg;

        public Report(final double sum, final double avg) {
            this.avg = avg;
            this.sum = sum;
        }

        public double getAvg() {
            return this.avg;
        }

        public double getSum() {
            return this.sum;
        }
    }
}
