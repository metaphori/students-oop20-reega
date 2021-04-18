package reega.controllers;

import reega.data.ContractController;
import reega.data.models.Contract;
import reega.data.models.gson.NewContract;
import reega.users.User;
import reega.viewutils.AbstractController;
import reega.viewutils.EventArgs;
import reega.viewutils.EventHandler;

import javax.inject.Inject;
import java.io.IOException;

public class ContractCreationControllerImpl extends AbstractController implements ContractCreationController{

        private User user;
        private ContractController contractController;
        private EventHandler<Contract> contractEventHandler;

        @Inject
        public ContractCreationControllerImpl(ContractController contractController) {
                this.contractController = contractController;
        }

        @Override
        public boolean registerContract(NewContract contract) throws IllegalArgumentException {
                try {
                        this.contractEventHandler.handle(new EventArgs<Contract>(this.contractController.addContract(contract), this));
                } catch (IOException e) {
                        throw new IllegalArgumentException(e.getMessage());
                }
                return true;
        }
        
        @Override
        public void setContractCreateEventHandler(EventHandler<Contract> eventHandler) {
                this.contractEventHandler = eventHandler;
        }

        @Override
        public void setUser(User newUser) {
                this.user = newUser;
        }

        @Override
        public User getUser() {
                return this.user;
        }
}
