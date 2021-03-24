package reega.controllers;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import reega.auth.AuthManager;
import reega.users.GenericUser;
import reega.util.FiscalCodeValidator;
import reega.util.ValueResult;
import reega.viewutils.AbstractController;
import reega.viewutils.Controller;
import reega.viewutils.ControllerChangedEventHandler;

public class LoginControllerImpl extends AbstractController implements LoginController {

    private String emailOrFiscalCode;
    private String password;
    private final AuthManager authManager;

    @Inject
    public LoginControllerImpl(final AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToRegistration() {
        this.pushController(RegistrationController.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmailOrFiscalCode(final String emailOrFiscalCode) {
        this.emailOrFiscalCode = emailOrFiscalCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Void> login(final boolean rememberMe) {
        if (StringUtils.isBlank(this.emailOrFiscalCode)) {
            return new ValueResult<>("You've not entered an email or a fiscal code");
        }
        if (StringUtils.isBlank(this.password)) {
            return new ValueResult<>("You've not entered a password");
        }

        Optional<GenericUser> user;
        if (EmailValidator.getInstance().isValid(this.emailOrFiscalCode)) {
            user = this.authManager.emailLogin(this.emailOrFiscalCode, this.password, rememberMe);
        } else if (FiscalCodeValidator.getInstance().isFiscalCodeValid(this.emailOrFiscalCode.toUpperCase())) {
            user = this.authManager.fiscalCodeLogin(this.emailOrFiscalCode.toUpperCase(), this.password, rememberMe);
        } else {
            return new ValueResult<>("Incorrect format for a fiscal code or an email");
        }

        if (user.isEmpty()) {
            return new ValueResult<>("Incorrect login credentials");
        }

        this.jumpToNextPage(user.get());

        // Return a valid result
        return new ValueResult<>((Void) null);
    }

    private void jumpToNextPage(final GenericUser user) {
        this.pushController(MainController.class, newController -> newController.user().setValue(user), true);
    }

    /**
     * Try the login without the password
     */
    private void tryLoginWithoutPassword() {
        // Try login without password
        this.authManager.tryLoginWithoutPassword().ifPresent(this::jumpToNextPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setControllerChangeEvent(final ControllerChangedEventHandler<Controller> controllerChangeEvent) {
        super.setControllerChangeEvent(controllerChangeEvent);
        this.tryLoginWithoutPassword();
    }
}
