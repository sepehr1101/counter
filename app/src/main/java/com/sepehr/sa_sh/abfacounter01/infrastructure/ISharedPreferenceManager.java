package com.sepehr.sa_sh.abfacounter01.infrastructure;

import java.lang.reflect.Type;

/**
 * Created by saeid on 3/12/2017.
 */
public interface ISharedPreferenceManager {
    void put(String key,String value);
    void put(String key,int value);
    void put(String key,boolean value);

    String getString(String key);
    int getInt(String key);
    boolean getBool(String key);

    void apply();
}
