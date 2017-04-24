package com.sepehr.sa_sh.abfacounter01;

import android.util.Base64;
import android.util.Base64InputStream;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by saeid on 9/9/2016.
 */
public class Crypto {

    private Crypto() {
    }
    //
    public static String encrypt(String password){
        String encodedPassword_1 = Base64.encodeToString( password.getBytes(), Base64.DEFAULT );
        String encodedPassword_2=Base64.encodeToString(encodedPassword_1.getBytes(),Base64.NO_CLOSE);
        return encodedPassword_2;
    }

    public static String decrypt(String encodedPassword){
        String encodedPassword_1 = new String( Base64.decode( encodedPassword, Base64.DEFAULT ));
        String encodedPassword_2 = new String( Base64.decode( encodedPassword_1, Base64.DEFAULT ));
        return encodedPassword_2;
    }
}
