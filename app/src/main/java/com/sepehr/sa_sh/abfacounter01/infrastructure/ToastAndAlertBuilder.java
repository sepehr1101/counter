package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by saeid on 3/2/2017.
 */
public class ToastAndAlertBuilder implements IToastAndAlertBuilder{
    Context appContext;
    public ToastAndAlertBuilder(Context appContext) {
        this.appContext=appContext;
    }

    public void makeSimpleAlert(String message){
        AlertDialog alert = new AlertDialog.Builder(appContext/*, R.drawable.border_dialog*/)
                .create();
        alert.setMessage(message);
        alert.show();
    }
    //
    public void makeSimpleToast(String message){
        Toast.makeText(appContext,message,Toast.LENGTH_LONG).show();
    }
}
