package reega.data;

import java.util.List;

import reega.data.models.Contract;

public interface ContractManager {
    /**
     * Fetch the contracts of the current user.
     *
     * @return the contracts of the current user
     */
    List<Contract> fetchUserContracts();
}
