package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.FlashLight;
import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by saeid on 5/23/2017.
 */

public class FlashLightFragment extends DialogFragment {

    ImageButton btnSwitch;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;
    MediaPlayer mp;
    public FlashLightFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_flash_light, container, false);

        btnSwitch = (ImageButton) rootView.findViewById(R.id.btnSwitch);
        hasFlash = getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(getContext())
                    .create();
            alert.setTitle("خطا");
            alert.setMessage("متاسفانه دیوایس شما از فلاش پشتیبانی نمیکند...");
            /*alert.setButton(1, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });*/
            alert.show();
            return rootView;
        }
        getCamera();

        // displaying button image
        toggleButtonImage();


        // Switch button click event to toggle flash on/off
        btnSwitch.setOnClickListener(new View.OnClickListener(){

                                         @Override
                                         public void onClick (View v){
                                             if (isFlashOn) {
                                                 // turn off flash
                                                 turnOffFlash();
                                             } else {
                                                 // turn on flash
                                                 turnOnFlash();
                                             }
                                         }
                                     }

        );
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        // get the camera
        //endregion

        return rootView;
    }
    //
    //region _______________________________ flash methods____________________________________
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error:", e.getMessage());
            }
        }
    }

    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            playSound();

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }

    }

    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            playSound();
            camera.setParameters(params);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }

            /*params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
*/

    // Playing sound
    // will play button toggle sound on flash on / off
    private void playSound() {
        if (isFlashOn) {
            mp = MediaPlayer.create(getContext(), R.raw.light_switch_off);
        } else {
            mp = MediaPlayer.create(getContext(), R.raw.light_switch_on);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    /*
     * Toggle switch button images
     * changing image states to on / off
     * */
    private void toggleButtonImage() {
        if (isFlashOn) {
            btnSwitch.setImageResource(R.drawable.btn_switch_on);
        } else {
            btnSwitch.setImageResource(R.drawable.btn_switch_off);
        }
    }

    //endregion
    //
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    public void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    public void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // on resume turn on the flash
        if (hasFlash)
            turnOnFlash();
    }
}
