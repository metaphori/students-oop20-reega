package reega.data.models.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * API data model
 */
public class UserAuthToken {
    @SerializedName("selector")
    public String selector;
    @SerializedName("validator")
    public String validator;

    public UserAuthToken(final String selector, final String validator) {
        this.selector = selector;
        this.validator = validator;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final UserAuthToken that = (UserAuthToken) o;
        return this.selector.equals(that.selector) && this.validator.equals(that.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.selector, this.validator);
    }
}
