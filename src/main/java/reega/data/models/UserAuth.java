package reega.data.models;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.hash.Hashing;

/**
 * This object is used to represent everything you need to implement the "remind-me" mechanism.
 *
 */
public final class UserAuth implements Serializable {
    private static final long serialVersionUID = -3406537385809547251L;
    private static final int SELECTOR_LENGTH = 12;
    private static final int VALIDATOR_LENGTH = 64;

    private final String selector;
    private final String validator;

    /**
     * Generate a UserAuth object containing selector and hashed validator.
     *
     * @param selector  selector
     * @param validator validator
     */
    public UserAuth(final String selector, final String validator) {
        this.selector = selector;
        this.validator = validator;
    }

    /**
     * Generate a UserAuth for the user with self generating selector and validator.
     */
    public UserAuth() {
        this(RandomStringUtils.random(UserAuth.SELECTOR_LENGTH),
                Hashing.sha256()
                        .hashString(RandomStringUtils.random(UserAuth.VALIDATOR_LENGTH), StandardCharsets.UTF_8)
                        .toString());
    }

    public String getSelector() {
        return this.selector;
    }

    public String getValidator() {
        return this.validator;
    }
}
