package reega.data;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.PriceModel;
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
     * List the available price models
     *
     * @return
     * @throws IOException
     * @throws SQLException
     */
    List<PriceModel> getPriceModels() throws IOException, SQLException;

    /**
     * Add price model
     *
     * @throws IOException
     * @throws SQLException
     */
    void addPriceModel(PriceModel priceModel) throws IOException, SQLException;

    /**
     * Remove the price model. DO NOT DELETE IF THERE IS A CONTRACT USING THE PRICE MODEL
     *
     * @param id
     * @throws IOException
     * @throws SQLException
     */
    void removePriceModel(int id) throws IOException, SQLException;

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
}
