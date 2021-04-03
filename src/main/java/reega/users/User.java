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

    default String getFullName() {
        return String.format("%s %s",this.getName(),this.getSurname());
    }
}
