package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.orm.util.MultiDexHelper;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by _1101 on 5/23/2017.
 */

@ReportsCrashes
    (
        formUri = "https://collector.tracepot.com/7c9a45da",
        //mailTo = "mantera.sh@hotmail.com",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. When defined, adds a user text field input with this text resource as a label
        //resDialogEmailPrompt = R.string.crash_user_email_label, // optional. When defined, adds a user email text entry with this text resource as label. The email address will be populated from SharedPreferences and will be provided as an ACRA field if configured.
        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
        //resDialogTheme = R.style.AppTheme_Dialog, //optional. default is Theme.Dialog

    )
public class MyApplication extends com.orm.SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        MultiDex.install(this);
    }
    public static Context getAppContext() {
        return MyApplication.getAppContext();
    }
}
