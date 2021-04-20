package reega.data;

import java.util.List;

import reega.data.models.Contract;
import reega.users.User;

public interface OperatorContractManager extends ContractManager {
    /**
     * Fetch the contracts of the user.
     *
     * @param user user used for finding contracts
     * @return a {@link List} containing the contract of <code>user</code>
     */
    List<Contract> fetchContractsByUser(User user);

    /**
     * Deleting a user contract.
     *
     * @param contract contract that needs to be deleted
     * @return returns true if it has been deleted, false otherwise
     */
    boolean deleteUserContract(Contract contract);
}
