package reega.data;

import reega.data.models.Data;

import java.util.List;

public interface OperatorDataFetcher extends DataFetcher {
    /**
     * Get the data of all the users
     * @return the data of all the users
     */
    List<Data> getGeneralData();
}
