package reega.data;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.PriceModel;
import reega.data.models.ServiceType;
import reega.data.remote.models.NewContract;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
     * Add contract
     *
     * @param contract
     * @throws IOException
     * @throws SQLException
     */
    void addContract(NewContract contract) throws IOException, SQLException;

    /**
     * List the available price models
     *
     * @return
     * @throws IOException
     * @throws SQLException
     */
    List<PriceModel> getPriceModels() throws IOException, SQLException;

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
