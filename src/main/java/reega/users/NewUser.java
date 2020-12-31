package reega.users;

import org.springframework.security.crypto.bcrypt.BCrypt;

public final class NewUser extends GenericUser {
	private final String passwordHash;

	public NewUser(final Role role, final String name, final String surname, final String email,
			final String fiscalCode, final String password) {
		super(role, name, surname, email, fiscalCode);
		this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	@Override
	public String getPasswordHash() {
		return this.passwordHash;
	}

}
