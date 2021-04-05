package reega.data.models.gson;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("role")
    public String role;
    @SerializedName("name")
    public String name;
    @SerializedName("surname")
    public String surname;
    @SerializedName("email")
    public String email;
    @SerializedName("fiscal_code")
    public String fiscalCode;
}
