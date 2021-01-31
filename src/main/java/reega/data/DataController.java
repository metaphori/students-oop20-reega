package reega.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;

/**
 * This controller handles all the data-based operations
 */
public interface DataController {

    /**
     * List all contracts for the user
     *
     * @return List<Contract>
     * @throws IOException
     * @throws SQLException
     */
    List<Contract> getUserContracts() throws IOException, SQLException;

    /**
     * Push data to the database (implementation specific)
     *
     * @param data
     * @throws SQLException
     */
    void putUserData(Data data) throws SQLException, IOException;

    /**
     * Get the latest timestamp for the specific contract and metric present in the database
     *
     * @param contractID
     * @return
     * @throws SQLException
     */
    Long getLatestData(int contractID, ServiceType service) throws SQLException, IOException;
}
