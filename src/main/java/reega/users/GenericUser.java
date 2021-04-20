package reega.users;

import java.util.Locale;

/**
 * Generic implementation of User.
 */
public class GenericUser implements User {
    private final Role role;
    private final String name;
    private final String surname;
    private final String email;
    private final String fiscalCode;

    public GenericUser(final Role role, final String name, final String surname, final String email,
            final String fiscalCode) {
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.fiscalCode = fiscalCode;
    }

    public GenericUser(final reega.data.models.gson.User user) {
        this(Role.valueOf(user.role.toUpperCase(Locale.US)), user.name, user.surname, user.email, user.fiscalCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRole() {
        return this.role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSurname() {
        return this.surname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFiscalCode() {
        return this.fiscalCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPasswordHash() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GenericUser [role=" + this.role.getRoleName() + ", name=" + this.name + ", surname=" + this.surname
                + ", email=" + this.email + ", fiscalCode=" + this.fiscalCode + "]";
    }
}
