package reega.data;

import java.io.IOException;
import java.util.List;

import reega.users.NewUser;
import reega.users.User;

public interface UserController {

    /**
     * Add user to REEGA platform.
     *
     * @param newUser new user that wants to be added to the platform
     */
    void addUser(NewUser newUser) throws IOException;

    /**
     * Remove user from REEGA platform.
     *
     * @param fiscalCode fiscal code of the user that wants to be removed
     * @throws IOException
     */
    void removeUser(String fiscalCode) throws IOException;

    /**
     * Find and return a user by a specified contract ID. Admin only
     *
     * @param contractID id of the contract
     * @return the accountholder user
     * @throws IOException
     */
    User getUserFromContract(int contractID) throws IOException;

    /**
     * Search for users with keyword matching in name, surname and fiscal code.
     *
     * @param keyword to match, case insensitive
     * @return list of users matching the keyword
     * @throws IOException
     */
    List<User> searchUser(String keyword) throws IOException;

}
