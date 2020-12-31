package reega.users;

public final class NewUser extends GenericUser {
	private final String passwordHash;

	public NewUser(final Role role, final String name, final String surname, final String email,
			final String fiscalCode, final String password) {
		super(role, name, surname, email, fiscalCode);
		this.passwordHash = password;
	}

	@Override
	public String getPasswordHash() {
		return this.passwordHash;
	}

}
