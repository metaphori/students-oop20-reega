package reega.controllers;

import java.io.IOException;

import javax.inject.Inject;

import reega.data.ContractController;
import reega.data.models.Contract;
import reega.data.models.gson.NewContract;
import reega.users.User;
import reega.viewutils.AbstractViewModel;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

public class ContractCreationViewModelImpl extends AbstractViewModel implements ContractCreationViewModel {

    private User user;
    private final ContractController contractController;
    private EventHandler<Contract> contractEventHandler;

    @Inject
    public ContractCreationViewModelImpl(final ContractController contractController) {
        this.contractController = contractController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean registerContract(final NewContract contract) {
        try {
            final Contract newContract = this.contractController.addContract(contract);
            if (newContract != null) {
                this.contractEventHandler.handle(new EventArgs<>(newContract, this));
            } else {
                return false;
            }
        } catch (final IOException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContractCreateEventHandler(final EventHandler<Contract> eventHandler) {
        this.contractEventHandler = eventHandler;
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
}
