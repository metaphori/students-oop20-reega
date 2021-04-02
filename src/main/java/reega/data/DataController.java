package reega.data;

import org.jetbrains.annotations.Nullable;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.gson.NewContract;

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
    List<Contract> getUserContracts() throws IOException;

    /**
     * retrieves all the contracts in the name of the specified user
     * @param userID
     * @return
     * @throws IOException
     */
    List<Contract> getContractsForUser(int userID) throws IOException;

    /**
     * @return
     * @throws IOException
     * @throws SQLException
     */
    List<Contract> getAllContracts() throws IOException, SQLException;

    /**
     * Add contract
     *
     * @param contract
     * @throws IOException
     * @throws SQLException
     */
    void addContract(NewContract contract) throws IOException, SQLException;

    /**
     * Delete contract with REEGA. It will also delete all the related data
     *
     * @param id
     * @throws IOException
     * @throws SQLException
     */
    void removeContract(int id) throws IOException, SQLException;

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
    Long getLatestData(int contractID, DataType service) throws SQLException, IOException;

    List<Data> getMonthlyData(@Nullable Integer contractID) throws IOException;
}
