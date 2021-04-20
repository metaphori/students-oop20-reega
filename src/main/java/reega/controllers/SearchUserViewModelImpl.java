package reega.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import reega.data.ContractController;
import reega.data.UserController;
import reega.data.models.Contract;
import reega.logging.ExceptionHandler;
import reega.users.User;
import reega.viewutils.AbstractViewModel;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

public class SearchUserViewModelImpl extends AbstractViewModel implements SearchUserViewModel {

    private EventHandler<User> userEventHandler;
    private EventHandler<Pair<User, Contract>> contractEventHandler;
    private final UserController userController;
    private final ContractController contractController;
    private final ExceptionHandler exceptionHandler;

    @Inject
    public SearchUserViewModelImpl(final UserController userController, final ContractController contractController,
            final ExceptionHandler exceptionHandler) {
        this.userController = userController;
        this.contractController = contractController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> searchUser(final String user) {
        try {
            return this.userController.searchUser(user);
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "couldn't search user from SearchUserController");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<User, Set<Contract>> searchContract(final String contract) {

        try {
            return this.contractController.searchContract(contract).stream().collect(Collectors.groupingBy(cnt -> {
                try {
                    return this.userController.getUserFromContract(cnt.getId());
                } catch (final IOException e) {
                    this.exceptionHandler.handleException(e, "couldn't search user from SearchUserController");
                }
                return null;
            })).entrySet().stream().collect(Collectors.toMap(Entry::getKey, pair -> new HashSet<>(pair.getValue())));
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "couldn't search contract from SearchUserController");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserFound(final User user) {
        this.userEventHandler.handle(new EventArgs<>(user, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContractFound(final User user, final Contract contract) {
        this.contractEventHandler.handle(new EventArgs<>(Pair.of(user, contract), this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserFoundEventHandler(final EventHandler<User> userEventHandler) {
        this.userEventHandler = userEventHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContractFoundEventHandler(final EventHandler<Pair<User, Contract>> contractEventHandler) {
        this.contractEventHandler = contractEventHandler;
    }

}
