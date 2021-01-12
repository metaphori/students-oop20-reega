package reega.controllers;

import reega.viewutils.AbstractController;

public class LoginControllerImpl extends AbstractController implements LoginController {
    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToRegistration() {
        this.pushController(RegistrationController.class);
    }
}
