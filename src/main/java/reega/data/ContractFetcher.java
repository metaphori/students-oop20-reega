package reega.data;

import reega.data.models.Contract;
import reega.users.User;

import java.util.List;

public interface ContractFetcher {
    /**
     * Fetch the contracts of the current user
     * @return the contracts of the current user
     */
    List<Contract> fetchUserContracts();
}
