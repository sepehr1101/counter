package com.sepehr.sa_sh.abfacounter01.Logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;
import com.sepehr.sa_sh.abfacounter01.BuildConfig;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.IAbfaService;
import com.sepehr.sa_sh.abfacounter01.NetworkHelper;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.CounterReportValueKeyViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.CounterStateValueKeyViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.KarbariViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.MobileInputModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.OnOffLoadViewModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.ReadingConfigViewModel;
import com.sepehr.sa_sh.abfacounter01.models.OffloadState;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterStateValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.HighLowModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by _1101 on 4/24/2017.
 */

/**
 *<h1>logic of loading data</h1>
 * <p>class to load counter reading data including
 * online , offline ,emergency ,special loading.
 * </p>
 *@author _1101
 *@version 1.0.
 *@since 2017-04-24
 */
public class ReloadLogic implements IOnLoadLogic{
    //declare variables
    Context mContext;
    Button mStartButton;
    ProgressBar mProgressBar;
    TextView mStateTextView;
    //
    int userCode;
    String token,deviceId;
    ReadingConfigService readingConfig;
    IToastAndAlertBuilder toastAndAlertBuilder;
    public ReloadLogic(Context mContext,
                       Button mStartButton,
                       ProgressBar mProgressBar,
                       TextView mStateTextView,
                       int userCode, String token,
                       String deviceId,
                       ReadingConfigService readingConfig,
                       IToastAndAlertBuilder toastAndAlertBuilder) {
        this.mContext = mContext;
        this.mStartButton = mStartButton;
        this.mProgressBar = mProgressBar;
        this.mStateTextView = mStateTextView;
        this.userCode = userCode;
        this.token = token;
        this.deviceId = deviceId;
        this.readingConfig = readingConfig;
        this.toastAndAlertBuilder = toastAndAlertBuilder;
        resetUiElements();
    }
    //
    public void start(boolean isLocal) {
        mProgressBar.setVisibility(View.VISIBLE);
        mStateTextView.setText("اتصال به سرور...");
        mStateTextView.setVisibility(View.VISIBLE);
        mStartButton.setEnabled(false);
        mStartButton.setVisibility(View.GONE);
        loadAndSaveData(isLocal);
    }
    //
    private void resetUiElements(){
        mProgressBar.setVisibility(View.GONE);
        mStateTextView.setVisibility(View.GONE);
        mStartButton.setEnabled(true);
        mStartButton.setVisibility(View.VISIBLE);
    }
    //
    private void setDoneUiElements(){
        mProgressBar.setVisibility(View.GONE);
        mStartButton.setEnabled(false);
        mStartButton.setVisibility(View.VISIBLE);
        mStateTextView.setTextSize(20);
    }
    //
    private void loadAndSaveData(boolean isLocal){
        Collection<BigDecimal> trackNumbers=readingConfig.getReadTrackNumbers();
        IAbfaService abfaService = NetworkHelper.getInstance(isLocal).create(IAbfaService.class);
        Call<MobileInputModel> call=
                abfaService.reload(token, userCode, deviceId,trackNumbers);
        call.enqueue(new Callback<MobileInputModel>() {
            @Override
            public void onResponse(Call<MobileInputModel> call,
                                   retrofit2.Response<MobileInputModel> response) {
                //
                //region_______________________ response implicit error___________________
                if(!response.isSuccessful()){
                    SimpleErrorHandler.APIError error=SimpleErrorHandler.parseError(response);
                    handleRetrofitResponseCode(response.code(),error.message());
                    mProgressBar.setIndeterminate(false);
                    mProgressBar.setProgress(100);
                    mProgressBar.setVisibility(View.GONE);
                    mStartButton.setEnabled(true);
                    mStartButton.setVisibility(View.VISIBLE);
                    mStateTextView.setVisibility(View.GONE);
                    return;
                }
                //endregion
                //
                MobileInputModel loadedData = response.body();
                mStateTextView.setText("در حال اعتبار سنجی داده های دریافتی");
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
        ReloadLogic.BackgroundSave task = new ReloadLogic.BackgroundSave();
        task.initialize(mProgressBar, mobileInputModels, mStateTextView,readingConfig.getMaxIndex());
        task.execute("start");
    }
    //
    private void handleRetrofitResponseCode(int responseCode,String responseErrorMessage) {
        String errorMessage = SimpleErrorHandler.getErrorMessage(responseCode);
        Log.e("error:", errorMessage);
        toastAndAlertBuilder.makeSimpleAlert(errorMessage+"\n"+responseErrorMessage);
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
            this.highLowInfo=new HighLowModel(mobileInputModel.highLowViewModel);
            this.maxReadingIndex=maxReadingIndex;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                int i=0;
                for (OnOffLoadViewModel onOffLoadViewModel : mobileInputModel.myWorks) {
                    OnOffLoadModel dbModel=new OnOffLoadModel(onOffLoadViewModel,i);
                    if(onOffLoadViewModel.OffLoad.CounterStateCode!=null){
                        dbModel.offLoadState= OffloadState.SENT_SUCCESSFULLY;
                    }
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
                long count=OnOffLoadModel.count(OnOffLoadModel.class);
                final boolean preferTruncate=count<1;

                SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                    @Override
                    public void manipulateInTransaction() {
                        CounterStateValueKeyModel.deleteAll(CounterStateValueKeyModel.class);
                        CounterReportValueKeyModel.deleteAll(CounterReportValueKeyModel.class);
                        KarbariModel.deleteAll(KarbariModel.class);
                        HighLowModel.deleteAll(HighLowModel.class);
                        if(preferTruncate){
                            OnOffLoadModel.deleteAll(OnOffLoadModel.class);
                        }
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
            mStateTextView.setText("ذخیره ...");
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

       /* private void deleteDuplicates(){
            SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                @Override
                public void manipulateInTransaction() {
                    CounterStateValueKeyModel.deleteAll(CounterStateValueKeyModel.class);
                    CounterReportValueKeyModel.deleteAll(CounterReportValueKeyModel.class);
                    KarbariModel.deleteAll(KarbariModel.class);
                    HighLowModel.deleteAll(HighLowModel.class);
                    SugarRecord.saveInTx(onOffLoads);
                    SugarRecord.saveInTx(readingConfigs);
                    SugarRecord.saveInTx(counterStateValueKeys);
                    SugarRecord.saveInTx(counterReportValueKeys);
                    SugarRecord.saveInTx(karbariInfos);
                    highLowInfo.save();
                }
            });
        }*/
    }
}
