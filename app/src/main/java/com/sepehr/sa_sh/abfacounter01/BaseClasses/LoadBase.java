package com.sepehr.sa_sh.abfacounter01.BaseClasses;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.Activities.LoadActivity;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DeviceSerialManager;
import com.sepehr.sa_sh.abfacounter01.Logic.IOnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.OnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.ReloadLogic;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

/**
 * Created by _1101 on 5/25/2017.
 */

public class LoadBase {
    Context mContext;
    Button startLoadButton;
    ProgressBar progressBar;
    TextView loadStateTextView;
    ImageView loadImageView;
    //
    int userCode;
    String token,deviceId;
    ReadingConfigService readingConfig;
    IToastAndAlertBuilder toastAndAlertBuilder;
    IOnLoadLogic onLoadLogic;
    boolean isLocal;

    public LoadBase(Context context, View rootView, String startButtonText,
                    int imageResource, boolean isLocal, boolean isReload) {
        mContext=context;
        this.isLocal=isLocal;
        initialize(rootView,startButtonText,imageResource,isReload);
    }

    public void initialize(View rootView,String startButtonText,int imageResource,boolean isReload){
        deviceId= DeviceSerialManager.getSerial(mContext);
        findAndSetViews(rootView);
        startLoadButton.setText(startButtonText);
        loadImageView.setImageResource(imageResource);
        progressBar.setScaleY(7f);
        userCode=((LoadActivity)mContext).getUserCode();
        token=((LoadActivity)mContext).getToken();
        toastAndAlertBuilder=new ToastAndAlertBuilder(mContext);
        readingConfig=new ReadingConfigService();

        if(isReload){
            onLoadLogic=new ReloadLogic(mContext,startLoadButton,progressBar,loadStateTextView,userCode,token,
                    deviceId,readingConfig,toastAndAlertBuilder);
        }else {
            onLoadLogic=new OnLoadLogic(mContext,startLoadButton,progressBar,loadStateTextView,userCode,token,
                    deviceId,readingConfig,toastAndAlertBuilder);
        }
        startLoadingButtonClickListener();
    }

    public void findAndSetViews(View rootView){
        startLoadButton=(Button)rootView.findViewById(R.id.startLoadButton);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadStateTextView=(TextView)rootView.findViewById(R.id.loadStateTextView);
        loadImageView=(ImageView)rootView.findViewById(R.id.loadImage);
    }

    private void startLoadingButtonClickListener(){
        startLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadLogic.start(isLocal);
            }
        });
    }
}
