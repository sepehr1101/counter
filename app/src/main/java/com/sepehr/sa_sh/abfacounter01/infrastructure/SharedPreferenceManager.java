package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by saeid on 3/12/2017.
 */
public class SharedPreferenceManager implements ISharedPreferenceManager{
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SharedPreferenceManager(Context appContext) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        this.editor = this.prefs.edit();
    }
    //
    public void putString(String key,String value){
        editor.putString(key,value);
    }
    public void putInt(String key,int value){
        editor.putInt(key, value);
    }
    public void apply(){
        editor.apply();
    }
    public String getString(String key){
        return prefs.getString(key,"");
    }
    public int getInt(String key){
        return  prefs.getInt(key,0);
    }
}
