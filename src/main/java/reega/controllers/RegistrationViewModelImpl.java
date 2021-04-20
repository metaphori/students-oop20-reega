package reega.controllers;

import javax.inject.Inject;

import org.apache.commons.validator.routines.EmailValidator;

import reega.auth.AuthManager;
import reega.users.NewUser;
import reega.users.Role;
import reega.util.FiscalCodeValidator;
import reega.util.ValueResult;
import reega.viewutils.AbstractViewModel;

public class RegistrationViewModelImpl extends AbstractViewModel implements RegistrationViewModel {

    private String name;
    private String surname;
    private String email;
    private String fiscalCode;
    private String password;
    private String confirmPassword;
    private final AuthManager authManager;

    @Inject
    public RegistrationViewModelImpl(final AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void jumpToLogin() {
        this.pushViewModel(LoginViewModel.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSurname(final String surname) {
        this.surname = surname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFiscalCode(final String fiscalCode) {
        this.fiscalCode = fiscalCode;
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
    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    private ValueResult<Void> validate() {
        final StringBuffer errors = new StringBuffer(117);
        if (this.name.isBlank()) {
            errors.append("Name is missing");
        }
        if (this.surname.isBlank()) {
            errors.append("Surname is missing");
        }
        if (this.email.isBlank()) {
            errors.append("Email is missing");
        }
        if (this.fiscalCode.isBlank()) {
            errors.append("Fiscal code is missing");
        }
        if (this.password.isBlank()) {
            errors.append("Password is missing");
        }
        if (this.confirmPassword.isBlank()) {
            errors.append("Confirm password is missing");
        }
        if (errors.length() != 0) {
            return new ValueResult<>(errors.toString());
        }
        return new ValueResult<>((Void) null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueResult<Void> register() {
        final ValueResult<Void> validateFieldsNotBlank = this.validate();
        if (validateFieldsNotBlank.isInvalid()) {
            return validateFieldsNotBlank;
        }

        if (!EmailValidator.getInstance().isValid(this.email)) {
            return new ValueResult<>("Invalid email");
        }

        if (!FiscalCodeValidator.isFiscalCodeValid(this.fiscalCode)) {
            return new ValueResult<>("Invalid fiscal code");
        }

        if (!this.password.equals(this.confirmPassword)) {
            return new ValueResult<>("Confirm password field and password field don't correspond");
        }

        final ValueResult<Void> createdUserResult = this.authManager.createUser(
                new NewUser(Role.USER, this.name, this.surname, this.email, this.fiscalCode, this.password));

        if (createdUserResult.isInvalid()) {
            return createdUserResult;
        }

        this.jumpToLogin();

        return createdUserResult;
    }
}
