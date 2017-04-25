package com.sepehr.sa_sh.abfacounter01;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.Logic.IOnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.OnLoadLogic;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.CounterReportValueKeyViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.CounterStateValueKeyViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.KarbariViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.MobileInputModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.OnOffLoadViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.ReadingConfigViewModel;
import com.sepehr.sa_sh.abfacounter01.models.SpecialLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterStateValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.HighLowModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by saeid on 11/9/2016.
 */
public class SpecialLoadFragment extends Fragment {
    public SpecialLoadFragment() {
    }
    //
    Typeface face;
    ProgressBar progressSpecialLoad;
    Button buttonSpecialLoad;
    TextView loadStateTextView;
    String token,deviceId;
    int userCode;
    IToastAndAlertBuilder toastAndAlertBuilder;
    ReadingConfigService readingConfig;
    IOnLoadLogic onLoadLogic;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_special_load, container, false);
        initialize(rootView);
        return rootView;
    }
    //
    private void initialize(View rootView){
        Context appContext=getContext();
        face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");
        progressSpecialLoad=(ProgressBar)rootView.findViewById(R.id.progressbarSpecialLoad);
        buttonSpecialLoad=(Button)rootView.findViewById(R.id.specialOffLoadButton);
        loadStateTextView=(TextView)rootView.findViewById(R.id.loadStateTextView);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        userCode=prefs.getInt("userCode",0);
        token=prefs.getString("token"," ");
        deviceId=Build.SERIAL;
        toastAndAlertBuilder=new ToastAndAlertBuilder(appContext);
        readingConfig=new ReadingConfigService();
        onLoadLogic=new OnLoadLogic(appContext,buttonSpecialLoad,progressSpecialLoad,loadStateTextView,userCode,token,
                deviceId,readingConfig,toastAndAlertBuilder);
        startSpecialLoadingButtonClickListener();
    }

    private void startSpecialLoadingButtonClickListener(){
        buttonSpecialLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadLogic.start(false);
            }
        });
    }
}
