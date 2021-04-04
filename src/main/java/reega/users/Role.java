package reega.users;

/**
 * User role types. The roleName value is the same as what is stored into the database
 */
public enum Role {
    ADMIN("admin"), USER("user");

    private final String roleName;

    Role(final String role) {
        this.roleName = role;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
