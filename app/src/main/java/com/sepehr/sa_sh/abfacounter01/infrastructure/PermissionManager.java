package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by saeid on 3/1/2017.
 */
public class PermissionManager {
    private PermissionManager() {
    }
    public static boolean DoIHaveThisPermission(Context appContext,String permission){
        int res = appContext.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
