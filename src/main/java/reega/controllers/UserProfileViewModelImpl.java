package reega.controllers;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import reega.data.UserController;
import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;
import reega.util.ValueResult;
import reega.viewutils.AbstractViewModel;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

public class UserProfileViewModelImpl extends AbstractViewModel implements UserProfileViewModel {

    private User user;
    private List<Contract> userContracts;
    private EventHandler<Contract> deleteUserContractHandler;
    private EventHandler<User> deleteUserHandler;
    private final UserController userController;
    private final ExceptionHandler exceptionHandler;

    @Inject
    public UserProfileViewModelImpl(final UserController userController, final ExceptionHandler exceptionHandler) {
        this.userController = userController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUser(final User newUser) {
        this.user = newUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser() {
        return this.user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserContracts(final List<Contract> contracts) {
        this.userContracts = contracts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contract> getUserContracts() {
        return this.userContracts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserContract(final Contract contract) {
        this.deleteUserContractHandler.handle(new EventArgs<>(contract, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDeleteUserContractHandler(final EventHandler<Contract> deleteUserContractHandler) {
        this.deleteUserContractHandler = deleteUserContractHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDeleteUserHandler(final EventHandler<User> deleteUserHandler) {
        this.deleteUserHandler = deleteUserHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Void> deleteCurrentUser() {
        try {
            this.userController.removeUser(this.user.getFiscalCode());
        } catch (final IOException e) {
            final String errString = "Error when deleting the current user";
            this.exceptionHandler.handleException(e, errString);
            return new ValueResult<>(null, errString);
        }
        this.deleteUserHandler.handle(new EventArgs<>(this.user, this));
        return new ValueResult<>((Void) null);
    }
}
