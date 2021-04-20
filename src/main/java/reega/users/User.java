package reega.users;

import org.apache.commons.lang3.StringUtils;

/**
 * Basic use interface containing the minimum user's info.
 */
public interface User {
    /**
     * Get the {@link Role} of the user.
     *
     * @return the {@link Role} of the user
     */
    Role getRole();

    /**
     * Get the name of the user.
     *
     * @return the name of the user
     */
    String getName();

    /**
     * Get the surname of the user.
     *
     * @return the surname of the user
     */
    String getSurname();

    /**
     * Get the email of the user.
     *
     * @return the email of the user
     */
    String getEmail();

    /**
     * Get the fiscal code of the user.
     *
     * @return the fiscal code of the user
     */
    String getFiscalCode();

    /**
     * Get the password hash of the user.
     *
     * @return the password hash of the user
     */
    String getPasswordHash();

    /**
     * Get the full name of the user.
     *
     * @return the full name of the user, with name and surname capitalized
     */
    default String getFullName() {
        return String.format("%s %s", StringUtils.capitalize(this.getName()),
                StringUtils.capitalize(this.getSurname()));
    }
}
