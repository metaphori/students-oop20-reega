package reega.data;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.gson.NewContract;

import javax.annotation.Nullable;
import java.io.IOException;
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
     */
    List<Contract> getUserContracts() throws IOException;

    /**
     * retrieves all the contracts in the name of the specified user
     *
     * @param fiscalCode
     * @return
     * @throws IOException
     */
    List<Contract> getContractsForUser(String fiscalCode) throws IOException;

    /**
     * @return
     * @throws IOException
     */
    List<Contract> getAllContracts() throws IOException;

    /**
     * Add contract
     *
     * @param contract
     * @throws IOException
     */
    void addContract(NewContract contract) throws IOException;

    /**
     * Delete contract with REEGA. It will also delete all the related data
     *
     * @param id
     * @throws IOException
     */
    void removeContract(int id) throws IOException;

    /**
     * Search for contracts with keyword matching in name, surname and fiscal code
     * of the accountholder and contract address
     * @param keyword to match, case insensitive
     * @return list of contracts matching the keyword
     * @throws IOException
     */
    List<Contract> searchContract(final String keyword) throws IOException;

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
