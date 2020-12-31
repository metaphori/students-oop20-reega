package reega.users;

public class GenericUser implements User {
	private final int id;
	private final Role role;
	private final String name;
	private final String surname;
	private final String email;
	private final String fiscalCode;

	public GenericUser(int id, final Role role, final String name, final String surname, final String email,
			final String fiscalCode) {
		this.id = id;
		this.role = role;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.fiscalCode = fiscalCode;
	}

	public GenericUser(final Role role, final String name, final String surname, final String email,
			final String fiscalCode) {
		this(0, role, name, surname, email, fiscalCode);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Role getRole() {
		return this.role;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getSurname() {
		return this.surname;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getFiscalCode() {
		return this.fiscalCode;
	}

	@Override
	public String getPasswordHash() {
		return null;
	}

	@Override
	public String toString() {
		return "GenericUser [role=" + role.getRoleName() + ", name=" + name + ", surname=" + surname + ", email="
				+ email + ", fiscalCode=" + fiscalCode + "]";
	}
}
