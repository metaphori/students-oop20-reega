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

    public LoginResponse(Integer id, String name, String surname, String email, String fiscalCode, String role, String jwt) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.role = role;
        this.jwt = jwt;
    }
}
