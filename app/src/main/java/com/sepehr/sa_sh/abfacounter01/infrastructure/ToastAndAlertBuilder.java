package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.MyApplication;
import com.sepehr.sa_sh.abfacounter01.R;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

/**
 * Created by saeid on 3/2/2017.
 */
public class ToastAndAlertBuilder implements IToastAndAlertBuilder{
    private Context appContext;
    public ToastAndAlertBuilder(Context appContext) {
        this.appContext=appContext;
    }

    public void makeSimpleAlert(String message){
        fillContextIfNull();

        new LovelyInfoDialog(appContext)
                .setTopColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_info_outline_white_36dp)
                .setNotShowAgainOptionChecked(true)
                .setMessage(message)
                .show();

       /* AlertDialog alert = new AlertDialog.Builder(appContext*//*, R.drawable.border_dialog*//*)
                .create();
        alert.setMessage(message);
        alert.show();*/
    }
    //

    public void makeSimpleAlert(String message,String title){
        fillContextIfNull();

        new LovelyInfoDialog(appContext)
                .setTopColorRes(R.color.colorPrimaryRed)
                .setIcon(R.drawable.ic_info_outline_white_36dp)
                //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                //.setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(true)
                .setTitle(title)
                .setMessage(message)
                .show();
      /*  AlertDialog alert = new AlertDialog.Builder(appContext)
                .create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.show();*/
    }

    public void makeSimpleToast(String message){
        fillContextIfNull();
        Toast.makeText(appContext,message,Toast.LENGTH_LONG).show();
    }
    private void fillContextIfNull(){
        if(this.appContext==null){
            appContext= MyApplication.getAppContext();
        }
    }
}
