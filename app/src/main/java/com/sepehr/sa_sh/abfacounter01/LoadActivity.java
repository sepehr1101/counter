package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.Logic.IOnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.OnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

import java.util.concurrent.ExecutionException;

public class LoadActivity extends BaseActivity{
    Button startLoadButton2;
    ProgressBar progressBar2;
    TextView loadStateTextView2;
    //
    int userCode;
    String token,deviceId;
    ReadingConfigService readingConfig;
    IToastAndAlertBuilder toastAndAlertBuilder;
    IOnLoadLogic onLoadLogic;
    boolean isLocal;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_load);
        return uiElementInActivity;
    }

    @Override
    protected void initialize(){
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        deviceId=Build.SERIAL;
        Context appContext=this;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        userCode=prefs.getInt("userCode",0);
        isLocal=prefs.getBoolean("isLocal",false);
        startLoadButton2=(Button)findViewById(R.id.startLoadButton2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        loadStateTextView2=(TextView)findViewById(R.id.loadStateTextView2);
        progressBar2.setScaleY(7f);
        userCode=getUserCode();
        token=getToken();
        toastAndAlertBuilder=new ToastAndAlertBuilder(appContext);
        readingConfig=new ReadingConfigService();
        onLoadLogic=new OnLoadLogic(appContext,startLoadButton2,progressBar2,loadStateTextView2,userCode,token,
                deviceId,readingConfig,toastAndAlertBuilder);
        startLoadingButtonClickListener();
    }
    TextView t;
    private void startLoadingButtonClickListener(){
        startLoadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setText("s");
                boolean isLocal=false;
                onLoadLogic.start(isLocal);
            }
        });
    }
}
