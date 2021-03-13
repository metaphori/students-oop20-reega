package reega.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import reega.users.User;
import reega.viewutils.AbstractController;

public class MainViewControllerImpl extends AbstractController implements MainViewController{
    final ObjectProperty<User> user = new SimpleObjectProperty<>();

    @Override
    public ObjectProperty<User> user() {
        return this.user;
    }
}
