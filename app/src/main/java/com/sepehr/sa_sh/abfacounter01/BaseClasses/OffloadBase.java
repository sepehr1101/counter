package com.sepehr.sa_sh.abfacounter01.BaseClasses;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.Activities.OffLoadActivity;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IOnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IStatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.OnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.StatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.DeviceSerialManager;
import com.sepehr.sa_sh.abfacounter01.Logic.IOffloadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.OffloadLogic;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

/**
 * Created by saeid on 5/25/2017.
 */

public class OffloadBase {

    Context mContext;
    Button mStartButton;
    ProgressBar mProgressBar;
    TextView mStateTextView;
    Spinner mSpinner;
    ImageView offloadImage;
    CheckBox mForceImageOffload;

    int userCode;
    String token;
    String deviceId;
    boolean isLocal;

    IReadingConfigService readingConfigService;
    IStatisticsRepo statisticsRepo;
    IOnOffloadService onOffloadService;
    ICounterReportService counterReportService;
    IToastAndAlertBuilder toastAndAlertBuilder;

    IOffloadLogic offloadLogic;

    public OffloadBase(Context context,View rootView,String startButtonText,int imageResorce,boolean isLocal) {
        mContext=context;
        this.isLocal=isLocal;
        initialize(rootView,startButtonText,imageResorce);
    }

    private void initialize(View rootView,String startButtonText,int imageResource){
        isLocal=false;
        offloadImage=(ImageView)rootView.findViewById(R.id.image_offload);
        offloadImage.setImageResource(imageResource);
        mProgressBar=(ProgressBar) rootView.findViewById(R.id.progressBar);
        mSpinner=(Spinner)rootView.findViewById(R.id.offloadSpinner);
        mStartButton=(Button)rootView.findViewById(R.id.offLoadButton);
        mStartButton.setText(startButtonText);
        mStateTextView=(TextView)rootView.findViewById(R.id.offLoadStateTextView);
        mForceImageOffload=(CheckBox)rootView.findViewById(R.id.forceImageOffload);
        userCode=((OffLoadActivity)mContext).getUserCode();
        token=((OffLoadActivity)mContext).getToken();
        deviceId= DeviceSerialManager.getSerial(mContext);
        toastAndAlertBuilder=new ToastAndAlertBuilder(mContext);
        readingConfigService=new ReadingConfigService();
        statisticsRepo=new StatisticsRepo(mContext);
        onOffloadService=new OnOffloadService(mContext);
        counterReportService=new CounterReportService();
        offloadLogic=new OffloadLogic(mContext,mStartButton,mProgressBar,mStateTextView,mSpinner,userCode,token,deviceId,
                readingConfigService,toastAndAlertBuilder,statisticsRepo,onOffloadService,counterReportService,mForceImageOffload);
        startOffload();
    }

    private void startOffload(){
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offloadLogic.start(isLocal);
            }
        });
    }
}
