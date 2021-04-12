package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.data.models.Contract;
import reega.users.User;
import reega.viewutils.AbstractController;

import java.util.List;

public class UserProfileControllerImpl extends AbstractController implements UserProfileController {

    final ObjectProperty<User> user = new SimpleObjectProperty<>();
    private List<Contract> userContracts;

    @Override
    public void setUser(User newUser) {
        this.user().set(newUser);
    }

    @Override
    public User getUser() {
        return this.user().get();
    }

    @Override
    public ObjectProperty<User> user() {
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
}
