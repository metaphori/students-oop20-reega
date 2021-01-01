package reega.users;

public interface User {
	public Role getRole();

	public String getName();

	public String getSurname();

	public String getEmail();

	public String getFiscalCode();

	public String getPasswordHash();

	int getId();
}
