package reega.data.models;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * This object is used to represent everything you need to implement the
 * "remind-me" mechanism.
 *
 * @author manu
 */
public final class UserAuth implements Serializable {
    private static final long serialVersionUID = -3406537385809547251L;

    private final String selector;
    private final String validator;

    /**
     * Generate a UserAuth object containing usedID, selector and hashed validator
     *
     * @param selector
     * @param validator
     */
    public UserAuth(final String selector, final String validator) {
        this.selector = selector;
        this.validator = validator;
    }

    /**
     * Generate a UserAuth for the user with self generating selector
     * and validator
     */
    public UserAuth() {
        // TODO use apache instead of google hashing
        this(RandomStringUtils.random(12),
                Hashing.sha256().hashString(RandomStringUtils.random(64), StandardCharsets.UTF_8).toString());
    }

    public String getSelector() {
        return selector;
    }

    public String getValidator() {
        return validator;
    }
}
