package reega.controllers;

import reega.util.ValueResult;
import reega.viewutils.ViewModel;

public interface RegistrationViewModel extends ViewModel {
    /**
     * Jump to the login page
     */
    void jumpToLogin();

    /**
     * Set the name
     *
     * @param name name to set
     */
    void setName(String name);

    /**
     * Set the surname
     *
     * @param surname surname to set
     */
    void setSurname(String surname);

    /**
     * Set the email
     *
     * @param email email to set
     */
    void setEmail(String email);

    /**
     * Set the fiscal code
     *
     * @param fiscalCode fiscal code to set
     */
    void setFiscalCode(String fiscalCode);

    /**
     * Set the password
     *
     * @param password password to set
     */
    void setPassword(String password);

    /**
     * Set the confirm password
     *
     * @param confirmPassword
     */
    void setConfirmPassword(String confirmPassword);

    /**
     * Register a new user
     */
    ValueResult<Void> register();
}
