package reega.data.remote;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import reega.data.DataController;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.ServiceType;

public final class RemoteDatabaseAPI implements DataController {
	private static RemoteDatabaseAPI INSTANCE;

	private RemoteDatabaseAPI() {
		// TODO
	}

	public synchronized static RemoteDatabaseAPI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RemoteDatabaseAPI();
		}

		return INSTANCE;
	}

	@Override
	public void putUserData(Data data) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getLatestData(int contractID, ServiceType service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contract> getUserContracts(int userID) throws IOException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
