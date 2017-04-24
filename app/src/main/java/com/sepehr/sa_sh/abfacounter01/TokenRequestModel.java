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

    //
    public TokenRequestModel() {
    }
}
