/**
 *
 */
package reega.util;

import java.util.Objects;

/**
 * @author Marco
 *
 */
public class ValueResult<T> {
    private final T value;
    private final String message;
    private final boolean valid;

    /**
     * Construct a not valid result
     *
     * @param value   resulting value
     * @param message message associated with the not valid result
     */
    public ValueResult(final T value, final String message) {
        Objects.requireNonNull(message);
        this.value = value;
        this.message = message;
        this.valid = false;
    }

    /**
     * Construct a valid result
     *
     * @param value resulting value
     */
    public ValueResult(final T value) {
        this.value = value;
        this.message = "";
        this.valid = true;
    }

    /**
     * Get the value resulted
     *
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Get the message associated with the value
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Check if the result is valid
     *
     * @return true if the result is valid, false if not
     */
    public boolean isValid() {
        return this.valid;
    }

}
