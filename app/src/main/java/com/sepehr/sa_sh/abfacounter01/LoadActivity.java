package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
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
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterStateValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.HighLowModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Callback;
import retrofit2.Call;

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

    private void startLoadingButtonClickListener(){
        startLoadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadLogic.start(false);
            }
        });
    }
}
