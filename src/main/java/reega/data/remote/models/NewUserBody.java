package reega.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class NewUserBody {
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
    @SerializedName("password")
    public String passwordHash;

    public NewUserBody(String name, String surname, String email, String fiscalCode, String role, String passwordHash) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.role = role;
        this.passwordHash = passwordHash;
    }
}
