package reega.controllers;

import java.util.Optional;

import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import reega.auth.AuthManager;
import reega.users.Role;
import reega.users.User;
import reega.util.FiscalCodeValidator;
import reega.util.ValueResult;
import reega.viewutils.AbstractViewModel;
import reega.viewutils.EventHandler;

public class LoginViewModelImpl extends AbstractViewModel implements LoginViewModel {

    private String emailOrFiscalCode;
    private String password;
    private final AuthManager authManager;

    @Inject
    public LoginViewModelImpl(final AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToRegistration() {
        this.pushViewModel(RegistrationViewModel.class, true);
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

        ValueResult<Optional<User>> user;
        if (EmailValidator.getInstance().isValid(this.emailOrFiscalCode)) {
            user = this.authManager.emailLogin(this.emailOrFiscalCode, this.password, rememberMe);
        } else if (FiscalCodeValidator.getInstance().isFiscalCodeValid(this.emailOrFiscalCode.toUpperCase())) {
            user = this.authManager.fiscalCodeLogin(this.emailOrFiscalCode.toUpperCase(), this.password, rememberMe);
        } else {
            return new ValueResult<>("Incorrect format for a fiscal code or an email");
        }

        if (user.isInvalid()) {
            return new ValueResult<>(user.getMessage());
        }

        if (user.getValue().isEmpty()) {
            return new ValueResult<>("Incorrect login credentials");
        }

        this.jumpToNextPage(user.getValue().get());

        // Return a valid result
        return new ValueResult<>((Void) null);
    }

    @Override
    public ValueResult<Void> tryLogin() {
        ValueResult<Optional<User>> tokenLogin = this.authManager.tryLoginWithoutPassword();
        if (tokenLogin.isValid()) {
            if (tokenLogin.getValue().isPresent()) {
                this.jumpToNextPage(tokenLogin.getValue().get());
            }
            return new ValueResult<>((Void)null);
        }
        return new ValueResult<>(null, tokenLogin.getMessage());
    }

    private void jumpToNextPage(final User user) {
        final EventHandler<Void> logoutEvtHandler = (evtArgs) -> this.authManager.logout();
        if (user.getRole() == Role.USER) {
            this.pushViewModel(MainViewModel.class, newViewModel -> {
                newViewModel.setUser(user);
                newViewModel.setOnLogout(logoutEvtHandler);
            }, true);
            return;
        }
        this.pushViewModel(OperatorMainViewModel.class, newViewModel -> {
            newViewModel.setUser(user);
            newViewModel.setOnLogout(logoutEvtHandler);
        }, true);

    }
}
