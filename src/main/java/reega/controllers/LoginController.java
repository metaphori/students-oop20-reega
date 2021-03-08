package reega.controllers;

import reega.util.ValueResult;
import reega.viewutils.Controller;

public interface LoginController extends Controller {
    /**
     * Try the login without the password
     */
    void tryLoginWithoutPassword();

    /**
     * Jump to the registration page
     */
    void jumpToRegistration();

    /**
     * Set the email or the fiscal code
     *
     * @param emailOrFiscalCode email or fiscal code
     */
    void setEmailOrFiscalCode(String emailOrFiscalCode);

    /**
     * Set the password
     *
     * @param password password to set
     */
    void setPassword(String password);

    /**
     * Login into the the app
     *
     * @return login into the app
     */
    ValueResult<Void> login(boolean rememberMe);
}
