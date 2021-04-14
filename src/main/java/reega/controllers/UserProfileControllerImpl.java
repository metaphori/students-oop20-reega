package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.data.models.Contract;
import reega.users.User;
import reega.viewutils.AbstractController;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

import java.util.List;

public class UserProfileControllerImpl extends AbstractController implements UserProfileController {

    private User user;
    private List<Contract> userContracts;
    private EventHandler<Contract> deleteUserContractHandler;

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
}
