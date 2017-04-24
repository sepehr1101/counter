package com.sepehr.sa_sh.abfacounter01;

import android.util.Base64;

import com.orm.SugarRecord;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Created by saeid on 9/9/2016.
 */
public class UserPassDbModel extends SugarRecord {
    String userName;
    String password;
    Integer userCode;
    //
    public UserPassDbModel() {
    }

    public UserPassDbModel(String userName, String password, Integer userCode) throws UnsupportedEncodingException{
        this.userName = userName=Crypto.encrypt(userName);
        this.password = Crypto.encrypt(password);
        this.userCode = userCode;
    }
}
