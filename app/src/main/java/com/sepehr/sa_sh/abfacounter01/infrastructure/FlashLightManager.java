package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

/**
 * Created by saeid on 6/15/2017.
 */

public class FlashLightManager {
    private static final FlashLightManager mInstance = new FlashLightManager();
    private static Context mContext;
    private static boolean isFlashOn=false;

    public static FlashLightManager getInstance(Context context) {
        mContext=context;
        return mInstance;
    }

    private FlashLightManager() {
    }

    @TargetApi(23)
    private static void turnOn(){
        CameraManager camManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            boolean flashShouldBecome=true;
            String[] cameraId = camManager.getCameraIdList();
            camManager.setTorchMode(cameraId[0], flashShouldBecome);   //Turn ON
            isFlashOn=flashShouldBecome;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(23)
    private static void turnOff(){
        CameraManager camManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            boolean flashShouldBecome=false;
            String[] cameraId = camManager.getCameraIdList();
            camManager.setTorchMode(cameraId[0], flashShouldBecome);   //Turn ON
            isFlashOn=flashShouldBecome;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean toggleFlash(){
        if(isFlashOn){
            turnOff();
            return false;
        }
        else {
            turnOn();
            return true;
        }
    }

}
