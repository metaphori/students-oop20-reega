package reega.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class UserAuthToken {
    @SerializedName("user_id")
    public Integer userID;
    @SerializedName("selector")
    public String selector;
    @SerializedName("validator")
    public String validator;

    public UserAuthToken(int userID, String selector, String validator) {
        this.userID = userID;
        this.selector = selector;
        this.validator = validator;
    }
}
