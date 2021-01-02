package reega.data.models;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.hash.Hashing;

/**
 * This object is used to represent everything you need to implement the
 * "remind-me" mechanism.
 * 
 * @author manu
 *
 */
public final class UserAuth implements Serializable {
	private static final long serialVersionUID = -3406537385809547251L;

	private final int userID;
	private final String selector;
	private final String validator;

	/**
	 * Generate a UserAuth object containing usedID, selector and hashed validator
	 * 
	 * @param userID
	 * @param selector
	 * @param validator
	 */
	public UserAuth(final int userID, final String selector, final String validator) {
		this.userID = userID;
		this.selector = selector;
		this.validator = validator;
	}

	/**
	 * Generate a UserAuth for the specified usedID with self generating selector
	 * and validator
	 * 
	 * @param userID
	 */
	public UserAuth(final int userID) {
		this(userID, RandomStringUtils.random(12),
				Hashing.sha256().hashString(RandomStringUtils.random(64), StandardCharsets.UTF_8).toString());
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
