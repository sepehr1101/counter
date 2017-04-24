package com.sepehr.sa_sh.abfacounter01.infrastructure;

/**
 * Created by saeid on 3/12/2017.
 */
public interface ISharedPreferenceManager {
    void putString(String key,String value);
    void putInt(String key,int value);
    String getString(String key);
    int getInt(String key);
    void apply();
}
