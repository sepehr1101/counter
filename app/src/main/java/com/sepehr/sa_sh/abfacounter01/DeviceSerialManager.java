package com.sepehr.sa_sh.abfacounter01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import static android.os.Build.UNKNOWN;

public class DeviceSerialManager {
    private DeviceSerialManager() {
    }
    @SuppressLint("HardwareIds")
    public static String getSerial(Context context) {
        String serial = Build.SERIAL;
        if (
            android.os.Build.VERSION.SDK_INT >= 30 ||
            serial == null ||
            serial.toLowerCase().equals(UNKNOWN) ||
            serial.toLowerCase() == UNKNOWN) {
                 serial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return serial;
    }
}
