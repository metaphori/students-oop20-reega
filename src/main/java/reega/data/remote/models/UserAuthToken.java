package reega.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class UserAuthToken {
    @SerializedName("selector")
    public String selector;
    @SerializedName("validator")
    public String validator;

    public UserAuthToken(String selector, String validator) {
        this.selector = selector;
        this.validator = validator;
    }
}
