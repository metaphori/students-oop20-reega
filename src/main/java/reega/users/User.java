package reega.users;

/**
 * Basic use interface containing the minimum user's info
 */
public interface User {
    Role getRole();

    String getName();

    String getSurname();

    String getEmail();

    String getFiscalCode();

    String getPasswordHash();
}
