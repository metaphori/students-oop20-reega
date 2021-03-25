package reega.data.models.gson;

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

    public LoginResponse(final Integer id, final String name, final String surname, final String email,
            final String fiscalCode, final String role, final String jwt) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.role = role;
        this.jwt = jwt;
    }
}
