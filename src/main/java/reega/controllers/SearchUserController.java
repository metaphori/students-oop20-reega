package reega.controllers;

import reega.users.User;
import reega.viewutils.Controller;
import reega.viewutils.EventHandler;

public interface SearchUserController extends Controller {
    void setUserFoundEventHandler(EventHandler<User> user);
}
