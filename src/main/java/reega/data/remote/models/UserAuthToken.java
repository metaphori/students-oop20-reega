package reega.data.remote.models;

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

    public UserAuthToken(String selector, String validator) {
        this.selector = selector;
        this.validator = validator;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthToken that = (UserAuthToken) o;
        return selector.equals(that.selector) && validator.equals(that.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selector, validator);
    }
}
