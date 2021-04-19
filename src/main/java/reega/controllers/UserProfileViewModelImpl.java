package reega.controllers;

import reega.data.UserController;
import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;
import reega.util.ValueResult;
import reega.viewutils.AbstractViewModel;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class UserProfileViewModelImpl extends AbstractViewModel implements UserProfileViewModel {

    private User user;
    private List<Contract> userContracts;
    private EventHandler<Contract> deleteUserContractHandler;
    private EventHandler<User> deleteUserHandler;
    private UserController userController;
    private ExceptionHandler exceptionHandler;

    @Inject
    public UserProfileViewModelImpl(UserController userController, ExceptionHandler exceptionHandler) {
        this.userController = userController;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void setUser(User newUser) {
        this.user = newUser;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUserContracts(List<Contract> contracts) {
        this.userContracts = contracts;
    }

    @Override
    public List<Contract> getUserContracts() {
        return this.userContracts;
    }

    @Override
    public void deleteUserContract(Contract contract) {
        this.deleteUserContractHandler.handle(new EventArgs<>(contract, this));
    }

    @Override
    public void setDeleteUserContractHandler(EventHandler<Contract> deleteUserContractHandler) {
        this.deleteUserContractHandler = deleteUserContractHandler;
    }

    @Override
    public void setDeleteUserHandler(EventHandler<User> deleteUserHandler) {
        this.deleteUserHandler = deleteUserHandler;
    }

    @Override
    public ValueResult<Void> deleteCurrentUser() {
        try {
            this.userController.removeUser(user.getFiscalCode());
        } catch (IOException e) {
            String errString = "Error when deleting the current user";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }
        this.deleteUserHandler.handle(new EventArgs<>(this.user, this));
        return new ValueResult<>((Void)null);
    }
}
