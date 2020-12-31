package reega.users;

public enum Role {
	ADMIN("admin"), OPERATOR("operator"), USER("user");

	private final String roleName;

	private Role(String role) {
		this.roleName = role;
	}

	public String getRoleName() {
		return this.roleName;
	}
}
