package reega.data;


import org.junit.jupiter.api.*;
import reega.data.mock.TestConnection;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;
import reega.users.NewUser;
import reega.users.Role;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Some functions of UserController are already tested in DataControllerTest
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    private RemoteConnection connection;
    private DataController dataController;
    private UserController userController;
    private AuthController authController;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        dataController = DataControllerFactory.getRemoteDatabaseController(connection);
        userController = UserControllerFactory.getRemoteUserController(connection);
        authController = AuthControllerFactory.getRemoteAuthController(connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void createUserAndContract() throws IOException {
        NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "TTT111", "PASSWORD");
        userController.addUser(newUser);

        List<ServiceType> services = List.of(
                ServiceType.ELECTRICITY
        );
        NewContract newContract = new NewContract("Via Zamboni, 33, 40126 Bologna BO", services, "TTT111", new Date(1614942000000L));
        dataController.addContract(newContract);

        var contracts = dataController.getContractsForUser("TTT111");
        assertEquals(1, contracts.size());
    }

    @Test
    @Order(2)
    public void getUserFromContract() throws IOException {
        connection.logout();
        var user = authController.emailLogin("test@reega.it", "PASSWORD");
        assertNotNull(user);
        var contracts = dataController.getUserContracts();
        assertEquals(1, contracts.size());
        connection.logout();
        user = authController.emailLogin("admin@reega.it", "AES_PASSWORD");
        assertNotNull(user);
        user = userController.getUserFromContract(contracts.get(0).getId());
        assertNotNull(user);
        assertNotNull(user.getEmail());
        assertEquals("test@reega.it", user.getEmail());
    }

    @Test
    @Order(3)
    public void searchForUserTest() throws IOException {
        var users = userController.searchUser("Zamboni");
        assertEquals(1, users.size());

        users = userController.searchUser("zamboni");
        assertEquals(1, users.size());

        users = userController.searchUser("TTT");
        assertEquals(1, users.size());

        users = userController.searchUser("reega");
        assertEquals(2, users.size());

        users = userController.searchUser("test");
        assertEquals(1, users.size());
    }
}
