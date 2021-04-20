package reega.data;

import java.util.List;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.users.User;

public interface DataFetcher {
    /**
     * Fetch all the data available for a given user.
     *
     * @param user      user used to get data for
     * @param contracts contracts used to fetch data(can be a portion of the user's contracts)
     * @return a {@link List} of data of the given user
     */
    List<Data> fetchAllUserData(User user, List<Contract> contracts);

    /**
     * Merge <code>contract</code>'s data with <code>oldData</code>.
     *
     * @param oldData  old data
     * @param contract contract used for merging data
     * @return a {@link List} of data merged with the data from <code>contract</code>
     */
    List<Data> pushAndFetchContract(List<Data> oldData, Contract contract);

    /**
     * Remove the <code>contract</code> from the old data.
     *
     * @param oldData  oldData
     * @param contract contract used for removing data
     * @return a {@link List} of data in which the <code>contract</code>'s data has been removed
     */
    List<Data> removeAndFetchContract(List<Data> oldData, Contract contract);
}
