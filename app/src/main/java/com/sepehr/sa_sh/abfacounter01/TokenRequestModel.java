package com.sepehr.sa_sh.abfacounter01;

/**
 * Created by saeid on 11/4/2016.
 */
public class TokenRequestModel {
    String username;
    String password;
    String grant_type;

    public TokenRequestModel(String username, String password, String grant_type) {
        this.username = username;
        this.password = password;
        this.grant_type = grant_type;
    }
    //

    public TokenRequestModel(String username, String password) {
        String emailTail=DifferentCompanyManager.getEmailTail
                (DifferentCompanyManager.getActiveCompanyName());
        this.username="m_"+username+emailTail;
        this.password = password;
        this.grant_type="password";
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

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}
