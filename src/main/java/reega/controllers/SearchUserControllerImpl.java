package reega.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.DataController;
import reega.data.UserController;
import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;
import reega.viewutils.AbstractController;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

public class SearchUserControllerImpl extends AbstractController implements SearchUserController {

    private EventHandler<User> userEventHandler;
    private EventHandler<Pair<User, Contract>> contractEventHandler;
    private UserController userController;
    private DataController dataController;
    private ExceptionHandler exceptionHandler;

    @Inject
    public SearchUserControllerImpl(UserController userController, DataController dataController,
            ExceptionHandler exceptionHandler) {
        this.userController = userController;
        this.dataController = dataController;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public List<User> searchUser(String user) {
        try {
            return userController.searchUser(user);
        } catch (IOException e) {
            this.exceptionHandler.handleException(e, "couldn't search user from SearchUserController");
        }
        return null;
    }

    @Override
    public Map<User, Set<Contract>> searchContract(String contract) {

        try {
            return dataController.searchContract(contract).stream().collect(Collectors.groupingBy(cnt -> {
                try {
                    return this.userController.getUserFromContract(cnt.getId());
                } catch (IOException e) {
                    exceptionHandler.handleException(e, "couldn't search user from SearchUserController");
                }
                return null;
            }))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(pair -> pair.getKey(), pair -> new HashSet<>(pair.getValue())));
        } catch (Exception e) {
            this.exceptionHandler.handleException(e, "couldn't search contract from SearchUserController");
        }
        return null;
    }

    @Override
    public void setUserFound(User user) {
        this.userEventHandler.handle(new EventArgs<User>(user, this));
    }

    @Override
    public void setContractFound(User user, Contract contract) {
        this.contractEventHandler.handle(new EventArgs<Pair<User, Contract>>(Pair.of(user, contract), this));
    }

    @Override
    public void setUserFoundEventHandler(EventHandler<User> userEventHandler) {
        this.userEventHandler = userEventHandler;
    }

    @Override
    public void setContractFoundEventHandler(EventHandler<Pair<User, Contract>> contractEventHandler) {
        this.contractEventHandler = contractEventHandler;
    }

}
