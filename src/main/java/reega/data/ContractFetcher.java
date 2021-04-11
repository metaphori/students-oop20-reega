package reega.data;

import reega.data.models.Contract;
import reega.users.User;

import java.util.List;

public interface ContractFetcher {
    List<Contract> fetchContractsByUser(User user);
}
