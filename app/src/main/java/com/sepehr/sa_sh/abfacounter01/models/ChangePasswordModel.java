package com.sepehr.sa_sh.abfacounter01.models;

/**
 * Created by saeid on 11/9/2016.
 */
public class ChangePasswordModel {
    String email;
    String oldPassword;
    String newPassword;
    String confirmPassword;

    public ChangePasswordModel(String email, String oldPassword,
                               String newPassword, String confirmPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
