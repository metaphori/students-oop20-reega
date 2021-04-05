package reega.data.models.gson;

import com.google.gson.annotations.SerializedName;

/**
 * API data model
 */
public class LoginResponse extends User {
    @SerializedName("id")
    public Integer id;
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
