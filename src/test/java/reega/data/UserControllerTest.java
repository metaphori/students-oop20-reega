package reega.data;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import reega.data.factory.AuthControllerFactory;
import reega.data.factory.ContractControllerFactory;
import reega.data.factory.UserControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;
import reega.users.NewUser;
import reega.users.Role;

/**
 * Some functions of UserController are already tested in DataControllerTest.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class UserControllerTest {
    private RemoteConnection connection;
    private ContractController contractController;
    private UserController userController;
    private AuthController authController;

    @BeforeAll
    public void setup() throws IOException {
        this.connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        this.contractController = ContractControllerFactory.getRemoteDatabaseController(this.connection);
        this.userController = UserControllerFactory.getRemoteUserController(this.connection);
        this.authController = AuthControllerFactory.getRemoteAuthController(this.connection);
    }

    @AfterAll
    public void cleanup() throws IOException {
        this.connection.getService().terminateTest().execute();
        this.connection.logout();
    }

    @Test
    @Order(1)
    public void createUserAndContract() throws IOException {
        final NewUser newUser = new NewUser(Role.USER, "test", "surname", "test@reega.it", "TTT111", "PASSWORD");
        this.userController.addUser(newUser);

        final List<ServiceType> services = List.of(ServiceType.ELECTRICITY);
        final NewContract newContract = new NewContract("Via Zamboni, 33, 40126 Bologna BO", services, "TTT111",
                new Date(1614942000000L));
        this.contractController.addContract(newContract);

        final var contracts = this.contractController.getContractsForUser("TTT111");
        Assertions.assertEquals(1, contracts.size());
    }

    @Test
    @Order(2)
    public void getUserFromContract() throws IOException {
        this.connection.logout();
        var user = this.authController.emailLogin("test@reega.it", "PASSWORD");
        Assertions.assertNotNull(user);
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertEquals(1, contracts.size());
        this.connection.logout();
        user = this.authController.emailLogin("admin@reega.it", "AES_PASSWORD");
        Assertions.assertNotNull(user);
        user = this.userController.getUserFromContract(contracts.get(0).getId());
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getEmail());
        Assertions.assertEquals("test@reega.it", user.getEmail());
    }

    @Test
    @Order(3)
    public void searchForUserTest() throws IOException {
        var users = this.userController.searchUser("Zamboni");
        Assertions.assertEquals(0, users.size());

        users = this.userController.searchUser("zamboni");
        Assertions.assertEquals(0, users.size());

        users = this.userController.searchUser("TTT");
        Assertions.assertEquals(1, users.size());

        users = this.userController.searchUser("reega");
        Assertions.assertEquals(3, users.size());

        users = this.userController.searchUser("test");
        Assertions.assertEquals(1, users.size());
    }
}
