package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.Logic.IOnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.OnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

/**
 * Created by _1101 on 10/20/2016.
 */
public class UrgentLoadFragment extends Fragment{
    Button startLoadButton;
    ProgressBar progressBar;
    TextView loadStateTextView;
    //
    int userCode;
    String token,deviceId;
    ReadingConfigService readingConfig;
    IToastAndAlertBuilder toastAndAlertBuilder;
    IOnLoadLogic onLoadLogic;
    boolean isLocal;
    public UrgentLoadFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_urgent_load, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView){
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        deviceId= Build.SERIAL;
        Context appContext=getContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        userCode=prefs.getInt("userCode",0);
        isLocal=prefs.getBoolean("isLocal",false);
        startLoadButton=(Button)rootView.findViewById(R.id.startLoadButton);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadStateTextView=(TextView)rootView.findViewById(R.id.loadStateTextView);
        progressBar.setScaleY(7f);
        userCode=((UrgentActivity)getContext()).getUserCode();
        token=((UrgentActivity)getContext()).getToken();
        toastAndAlertBuilder=new ToastAndAlertBuilder(appContext);
        readingConfig=new ReadingConfigService();
        onLoadLogic=new OnLoadLogic(appContext,startLoadButton,progressBar,loadStateTextView,userCode,token,
                deviceId,readingConfig,toastAndAlertBuilder);
        startLoadingButtonClickListener();
    }
    private void startLoadingButtonClickListener(){
        startLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLocal=true;
                onLoadLogic.start(isLocal);
            }
        });
    }
}
