package reega.data.remote.models;

import com.google.gson.annotations.SerializedName;

/**
 * API data model
 */
public class LoginResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("surname")
    public String surname;
    @SerializedName("email")
    public String email;
    @SerializedName("fiscal_code")
    public String fiscalCode;
    @SerializedName("role")
    public String role;
    @SerializedName("jwt")
    public String jwt;
}
