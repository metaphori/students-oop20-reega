package reega.data;

import java.io.IOException;
import java.sql.SQLException;

import reega.data.models.UserAuth;
import reega.users.GenericUser;
import reega.users.NewUser;

final class RemoteAuthAPI implements AuthController {

	@Override
	public void addUser(NewUser newUser) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public GenericUser emailLogin(String email, String hash) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericUser fiscalCodeLogin(String fiscalCode, String hash) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericUser tokenLogin(UserAuth credentials) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeUserCredentials(int userID, String selector, String validator) throws SQLException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void userLogout(int userID) throws SQLException {
		// TODO Auto-generated method stub

	}

}
