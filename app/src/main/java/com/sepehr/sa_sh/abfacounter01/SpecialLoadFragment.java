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
