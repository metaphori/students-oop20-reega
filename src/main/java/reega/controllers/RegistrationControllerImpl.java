package reega.controllers;

import reega.viewutils.AbstractController;

public class RegistrationControllerImpl extends AbstractController implements RegistrationController {
    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToLogin() {
        this.pushController(LoginController.class);
    }
}
