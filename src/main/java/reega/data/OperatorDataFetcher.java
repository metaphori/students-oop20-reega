package reega.data;

import java.util.List;

import reega.data.models.Data;

public interface OperatorDataFetcher extends DataFetcher {
    /**
     * Get the data of all the users.
     *
     * @return the data of all the users
     */
    List<Data> getGeneralData();
}
