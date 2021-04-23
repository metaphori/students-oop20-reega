package reega.data;

import reega.data.models.Data;
import reega.data.models.DataType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public interface DataController {
    /**
     * Push data into the database (implementation specific).
     *
     * @param data data that needs to be put in the database
     */
    void putUserData(Data data) throws IOException;

    /**
     * Get the latest timestamp for the specific contract and metric present in the database.
     *
     * @param contractID ID of the researched contract
     * @param service    data type requested
     * @return the latest timestamp(in milliseconds) for the specific contract and {@link DataType}
     */
    Long getLatestData(int contractID, DataType service) throws IOException;

    /**
     * Get the data from the first day of the month until today for a given <code>contractID</code>.
     *
     * @param contractID contractID that needs to get the monthly data
     * @return a {@link List} of data containing data from the first day of the month until today
     * @throws IOException if an error occurred while performing the HTTP call
     */
    List<Data> getMonthlyData(@Nullable Integer contractID) throws IOException;
}
