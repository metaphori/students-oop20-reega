package reega.data;

import org.jetbrains.annotations.NotNull;
import reega.data.models.BaseContract;
import reega.data.models.Contract;
import reega.data.models.MonthlyReport;
import reega.data.models.gson.NewContract;

import java.io.IOException;
import java.util.List;

/**
 * This controller handles all the data-based operations.
 */
public interface ContractController {

    /**
     * List all contracts for the user.
     *
     * @return List of {@link BaseContract}
     * @throws IOException if an error occurred while performing the HTTP call
     */
    List<Contract> getUserContracts() throws IOException;

    /**
     * Retrieve all the contracts in the name of the specified user.
     *
     * @param fiscalCode fiscal code of the user
     * @return a {@link List} of all the contracts of the user
     * @throws IOException if an error occurred while performing the HTTP call
     */
    List<Contract> getContractsForUser(String fiscalCode) throws IOException;

    /**
     * Get all the contracts of the Reega platform.
     *
     * @return all the contracts of the Reega platform
     * @throws IOException if an error occurred while performing the HTTP call
     */
    List<Contract> getAllContracts() throws IOException;

    /**
     * Add contract.
     *
     * @param contract contract that needs to be added
     * @return the new contract
     * @throws IOException if an error occurred while performing the HTTP call
     */
    Contract addContract(NewContract contract) throws IOException;

    /**
     * Delete contract with REEGA. It will also delete all the related data
     *
     * @param id contract ID
     * @throws IOException if an error occurred while performing the HTTP call
     */
    void removeContract(int id) throws IOException;

    /**
     * Search for contracts with keyword matching in name, surname and fiscal code of the accountholder and contract
     * address.
     *
     * @param keyword to match, case insensitive
     * @return list of contracts matching the keyword
     * @throws IOException if an error occurred while performing the HTTP call
     */
    List<Contract> searchContract(String keyword) throws IOException;

    /**
     * Get all the bills for the contract IDs specified by <code>contractIDs</code>.
     *
     * @param contractIDs list of contract IDs that needs to get the bills
     * @return a list of {@link MonthlyReport}
     * @throws IOException if an error occurred while performing the HTTP call
     */
    List<MonthlyReport> getBillsForContracts(@NotNull List<Integer> contractIDs) throws IOException;
}
