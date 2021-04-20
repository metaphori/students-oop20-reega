package reega.data.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;

import reega.main.Settings;

public class MonthlyReport {
    private final String month;
    private final Map<DataType, Report> reports;

    public MonthlyReport(final long date, final Map<DataType, Report> reports) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        format.setTimeZone(TimeZone.getTimeZone(Settings.CLIENT_TIMEZONE));
        this.month = format.format(new Date(date));

        this.reports = reports;
    }

    /**
     * Get the month of the report.
     *
     * @return the month of the report
     */
    public String getMonth() {
        return this.month;
    }

    /**
     * Get all the reports by {@link DataType}.
     *
     * @return a {@link Map} that have the {@link DataType} as key and {@link Report} as value
     */
    public Map<DataType, Report> getReports() {
        return this.reports;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Record class used for describing a report summary.
     */
    public static class Report {
        private final double sum;
        private final double avg;

        public Report(final double sum, final double avg) {
            this.avg = avg;
            this.sum = sum;
        }

        /**
         * Get the average.
         *
         * @return the average
         */
        public double getAvg() {
            return this.avg;
        }

        /**
         * Get the total.
         *
         * @return the total
         */
        public double getSum() {
            return this.sum;
        }
    }
}
