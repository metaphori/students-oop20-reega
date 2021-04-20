package reega.users;

/**
 * User role types. The roleName value is the same as what is stored into the database
 */
public enum Role {
    /**
     * Administrator user.
     */
    ADMIN("admin"),
    /**
     * Common user.
     */
    USER("user");

    private final String roleName;

    Role(final String role) {
        this.roleName = role;
    }

    /**
     * Get the role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return this.roleName;
    }
}
