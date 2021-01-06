package reega.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;

public interface DataController {

	/**
	 * List all contracts for a specific user
	 * 
	 * @param userID
	 * @return List<Contract>
	 * @throws IOException
	 * @throws SQLException
	 */
	public List<Contract> getUserContracts(int userID) throws IOException, SQLException;

	/**
	 * 
	 * @param data
	 * @throws SQLException
	 */
	public void putUserData(Data data) throws SQLException;

	/**
	 * 
	 * @param contractID
	 * @return
	 * @throws SQLException
	 */
	public Long getLatestData(int contractID, ServiceType service) throws SQLException;
}
