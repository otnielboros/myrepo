package mobileapps.otniel.lab2.api;

public class TokenRequestBody {
    private String grant_type,username,password;

    public String getGrantType() {
        return grant_type;
    }

    public void setGrantType(String grantType) {
        this.grant_type = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TokenRequestBody(String grantType, String username, String password) {

        this.grant_type = grantType;
        this.username = username;
        this.password = password;
    }
}
