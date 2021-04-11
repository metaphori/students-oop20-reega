package reega.data;

import reega.data.models.Data;
import reega.data.models.DataType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public interface DataController {
    /**
     * Push data to the database (implementation specific)
     *
     * @param data
     */
    void putUserData(Data data) throws IOException;

    /**
     * Get the latest timestamp for the specific contract and metric present in the database
     *
     * @param contractID
     * @return
     */
    Long getLatestData(int contractID, DataType service) throws IOException;

    List<Data> getMonthlyData(@Nullable Integer contractID) throws IOException;

}
