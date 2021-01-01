package reega.data.models;

import java.io.Serializable;

public final class UserAuth implements Serializable {
	private static final long serialVersionUID = -3406537385809547251L;

	private final int userID;
	private final String selector;
	private final String validator;

	public UserAuth(final int userID, final String selector, final String validator) {
		this.userID = userID;
		this.selector = selector;
		this.validator = validator;
	}

	public int getUserID() {
		return userID;
	}

	public String getSelector() {
		return selector;
	}

	public String getValidator() {
		return validator;
	}
}
