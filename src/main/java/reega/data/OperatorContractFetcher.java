package reega.data;

import reega.data.models.Contract;
import reega.users.User;

import java.util.List;

public interface OperatorContractFetcher extends ContractFetcher{
    /**
     * Fetch the contracts of the user
     * @param user user used for finding contracts
     * @return a {@link List} containing the contract of <code>user</code>
     */
    List<Contract> fetchContractsByUser(User user);
}
