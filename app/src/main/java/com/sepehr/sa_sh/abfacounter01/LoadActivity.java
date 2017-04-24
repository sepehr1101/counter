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
    //
    IToastAndAlertBuilder toastAndAlertBuilder;

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

        //region ________________Load Button_______________

        startLoadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar2.setVisibility(View.VISIBLE);
                loadStateTextView2.setText("در حال بارگیری اطلاعات");
                loadStateTextView2.setVisibility(View.VISIBLE);
                startLoadButton2.setEnabled(false);
                startLoadButton2.setVisibility(View.GONE);
                loadAndSaveData(userCode);
            }
        });
        //endregion
    }
    //
    public void resetUiElements(){
        progressBar2.setVisibility(View.GONE);
        loadStateTextView2.setVisibility(View.GONE);
        startLoadButton2.setEnabled(true);
        startLoadButton2.setVisibility(View.VISIBLE);
    }
    //
    public void setDoneUiElements(){
        progressBar2.setVisibility(View.GONE);
        startLoadButton2.setEnabled(false);
        startLoadButton2.setVisibility(View.VISIBLE);
        loadStateTextView2.setTextSize(20);
    }
    //
    private void loadAndSaveData(int userCode){
        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        Call<MobileInputModel> call=
                abfaService.loadData(token, userCode, deviceId, BuildConfig.VERSION_CODE);
        call.enqueue(new Callback<MobileInputModel>() {
            @Override
            public void onResponse(Call<MobileInputModel> call,
                                   retrofit2.Response<MobileInputModel> response) {
                int responseCode=response.code();
                //
                //region_______________________ response implicit error___________________
                if(responseCode!=200){
                    handleRetrofitResponseCode(responseCode);
                    progressBar2.setIndeterminate(false);
                    progressBar2.setProgress(100);
                    progressBar2.setVisibility(View.GONE);
                    startLoadButton2.setEnabled(true);
                    startLoadButton2.setVisibility(View.GONE);
                    return;
                }
                //endregion
                //
                MobileInputModel loadedData = response.body();
                loadStateTextView2.setText("در حال اعتبار سنجی داده های دریافتی");
                validateMyWorks(loadedData);
            }

            @Override
            public void onFailure(Call<MobileInputModel> call, Throwable t) {
                resetUiElements();
                toastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
                Log.e("retrofit error", t.toString());
            }
        });
    }
    //
    private void validateMyWorks(final MobileInputModel mobileInputModels){
        BackgroundSave task = new BackgroundSave();
        task.initialize(progressBar2, mobileInputModels, loadStateTextView2,readingConfig.getMaxIndex());
        task.execute("start");
    }
    //
    private void handleRetrofitResponseCode(int responseCode) {
        String errorMessage = SimpleErrorHandler.getErrorMessage(responseCode);
        Log.e("error:", errorMessage);
        toastAndAlertBuilder.makeSimpleAlert(errorMessage);
    }
    //
    private class BackgroundSave extends AsyncTask<String, Integer, String> {
        ProgressBar progressBar;
        TextView textView;
        MobileInputModel mobileInputModel;
        List<OnOffLoadModel> onOffLoads =new ArrayList<>();
        List<ReadingConfigModel> readingConfigs=new ArrayList<>();
        HighLowModel highLowInfo;
        List<CounterStateValueKeyModel> counterStateValueKeys=new ArrayList<>();
        List<CounterReportValueKeyModel> counterReportValueKeys=new ArrayList<>();
        List<KarbariModel> karbariInfos=new ArrayList<>();
        int maxReadingIndex;

        public void initialize(ProgressBar progressBar, MobileInputModel mobileInputModel,TextView textView,int maxReadingIndex) {
            this.progressBar = progressBar;
            this.mobileInputModel = mobileInputModel;
            this.textView=textView;
            //this.readingConfigs=new ReadingConfigModel(mobileInputModel.readingConfigs);
            this.highLowInfo=new HighLowModel(mobileInputModel.highLowViewModel);
            this.maxReadingIndex=maxReadingIndex;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                int i=0;
                for (OnOffLoadViewModel onOffLoadViewModel : mobileInputModel.myWorks) {
                    OnOffLoadModel dbModel=new OnOffLoadModel(onOffLoadViewModel,i);
                    dbModel._index=i;
                    publishProgress(i);
                    onOffLoads.add(dbModel);
                    i++;
                }
                i=maxReadingIndex;
                for (ReadingConfigViewModel readingConfigFromNet: mobileInputModel.readingConfigs) {
                    boolean isActive=i==1?true:false;
                    ReadingConfigModel readingConfig=new ReadingConfigModel(readingConfigFromNet,i,isActive);
                    this.readingConfigs.add(readingConfig);
                    i++;
                }
                for(CounterStateValueKeyViewModel counterStateValueKeyViewModel:
                        mobileInputModel.counterStateValueKeys) {
                    CounterStateValueKeyModel counterStateValueKeyModel =
                            new CounterStateValueKeyModel(counterStateValueKeyViewModel);
                    this.counterStateValueKeys.add(counterStateValueKeyModel);
                }
                for (CounterReportValueKeyViewModel counterReportValueKeyViewModel:
                        mobileInputModel.reportValueKeys) {
                    CounterReportValueKeyModel counterReportValueKeyModel=
                            new CounterReportValueKeyModel(counterReportValueKeyViewModel);
                    this.counterReportValueKeys.add(counterReportValueKeyModel);
                }
                for (KarbariViewModel karbariViewModel:mobileInputModel.karbariInfos) {
                    KarbariModel karbari=new KarbariModel(karbariViewModel);
                    this.karbariInfos.add(karbari);
                }
                highLowInfo=new HighLowModel(mobileInputModel.highLowViewModel);

                SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                    @Override
                    public void manipulateInTransaction() {
                        //OnOffLoadModel.deleteAll(OnOffLoadModel.class);
                        //CapturedImageModel.deleteAll(CapturedImageModel.class);
                        //CounterReadingReport.deleteAll(CounterReadingReport.class);
                        //ReadingConfigModel.deleteAll(ReadingConfigModel.class);
                        CounterStateValueKeyModel.deleteAll(CounterStateValueKeyModel.class);
                        CounterReportValueKeyModel.deleteAll(CounterReportValueKeyModel.class);
                        KarbariModel.deleteAll(KarbariModel.class);
                        SugarRecord.saveInTx(onOffLoads);
                        SugarRecord.saveInTx(readingConfigs);
                        SugarRecord.saveInTx(counterStateValueKeys);
                        SugarRecord.saveInTx(counterReportValueKeys);
                        SugarRecord.saveInTx(karbariInfos);
                        highLowInfo.save();
                    }
                });

            } catch (Exception e) {
                Log.e("async error",e.getMessage());
                return "error";
            }

            return "exec";
        }

        @Override
        protected void onPostExecute(String result) {
            setDoneUiElements();
            startLoadButton2.setVisibility(View.GONE);
            if(result.equals("error")){
                textView.setText("خطا در ذخیره داده های بارگیری شده");
            }
            else {
                textView.setText("همکار گرامی" +" "+ "تعداد " + " " +
                        mobileInputModel.myWorks.size() + " " + "رکورد"+" "+"و"+" "+readingConfigs.size()+
                        " "+"مسیر بارگیری شد");
            }
        }

        @Override
        protected void onPreExecute() {
            loadStateTextView2.setText("ذخیره ...");
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    //
}
