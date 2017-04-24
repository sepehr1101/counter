package com.sepehr.sa_sh.abfacounter01;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orm.SugarTransactionHelper;
import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IOnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IStatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.OnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.StatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class OffLoadActivity extends BaseActivity{
    Button offLoadButton;
    ProgressBar offLoadProgressbar;
    TextView offLoadStateTextView;
    int userCode;
    String token;
    Spinner listNumberSpinner;
    ReadingConfigService readingConfigManager;
    List<String> listNumbers;
    IStatisticsRepo statisticsRepo;
    IOnOffloadService onOffloadService;
    ICounterReportService counterReportService;
    //

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_off_load);
        return uiElementInActivity;
    }
    @Override
    protected void initialize(){
        offLoadProgressbar=(ProgressBar)findViewById(R.id.progressBarOffLoad);
        offLoadStateTextView=(TextView)findViewById(R.id.offLoadStateTextView);
        offLoadProgressbar.setScaleY(7f);
        offLoadButton=(Button) findViewById(R.id.offLoadButton);
        userCode=getUserCode();
        token=getToken();
        onOffloadButtonClick();
        readingConfigManager=new ReadingConfigService();
        listNumbers=readingConfigManager.getListNumbers();
        listNumberSpinner=(Spinner)findViewById(R.id.listNumberSpinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_item, listNumbers);
        adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        listNumberSpinner.setAdapter(adapter);
        statisticsRepo =new StatisticsRepo(this);
        toastAndAlertBuilder=new ToastAndAlertBuilder(this);
        onOffloadService =new OnOffloadService(this);
        counterReportService =new CounterReportService();
    }
    //
    private void switchVisibility(){
        if(offLoadButton.getVisibility()==View.VISIBLE){
            offLoadProgressbar.setVisibility(View.VISIBLE);
            offLoadStateTextView.setVisibility(View.VISIBLE);
            offLoadButton.setEnabled(false);
            offLoadButton.setVisibility(View.GONE);
        }
        else if(offLoadButton.getVisibility()==View.GONE){
            offLoadProgressbar.setVisibility(View.GONE);
            offLoadStateTextView.setVisibility(View.GONE);
            offLoadButton.setEnabled(true);
            offLoadButton.setVisibility(View.VISIBLE);
        }
    }

    private void onOffloadButtonClick(){
        try {
            offLoadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!hasSpinnerValidItem()){
                        return;
                    }
                    switchVisibility();
                    boolean isAlalHesabSizeValid=isAlalHesabSizeValid();
                    if (!isAlalHesabSizeValid) {
                        String errorMessage="درصد علی الحساب و خوانده نشده بالاتر ازحد مجاز";
                        toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                        switchVisibility();
                        return;
                    }
                    doOffLoadCounterReadingTable();
                }
            });
        }
        catch (Exception e){
            Log.e("offload error", e.getMessage());
            toastAndAlertBuilder.makeSimpleAlert("خطا حین تخلیه اطلاعات");
        }
    }

    //
    private void doOffLoadCounterReadingTable() {
        String deviceId= Build.SERIAL;

        final ReadingConfigModel readingConfig = getValidReadingConfig();
        final List<OnOffLoadModel> counterReadingModel01s=
                onOffloadService.getNeedToBeOffloadedOverall(readingConfig.get_index());

        final List<CounterReadingReport> readingReports=
                counterReportService.getNeedToBeOffloadedOverall(readingConfig.getTrackNumber());

        Output output=new Output(readingReports,counterReadingModel01s);
        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        Call<Integer> call=abfaService.sendCounterReadingInfo(token,output,deviceId,userCode,true,readingConfig.getTrackNumber());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call,
                                   retrofit2.Response<Integer> response) {
                Integer count = response.body();
                int responseCode = response.code();
                String errorMessage = "";
                if (responseCode != 200) {
                    if (responseCode == 500) {
                        errorMessage = "خطای سرور";
                    } else if (responseCode == 404) {
                        errorMessage = "خطای آدرس";
                    } else if (responseCode == 401) {
                        errorMessage = "همکار گرامی از ثبت شدن دستگاه خود در سامانه قرائت اطمینان حاصل فرمایید";
                    }
                    Log.e("error:", errorMessage);
                    toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                    switchVisibility();
                    return;
                }

                doPostOffload(readingConfig);

                if (count == 0) {
                    offLoadStateTextView.setText("قبلا اطلاعات تخلیه شده ، دستگاه شما آماده بارگیری اطلاعات جدید میباشد");
                    toastAndAlertBuilder.makeSimpleAlert("تخلیه انجام شد ، همکار گرامی خدا قوت");
                } else {
                    offLoadStateTextView.setText("تخلیه به اتمام رسید" + " " + "تعداد" + " " +
                            count.toString() + " " + "رکورد تخلیه شد");
                }
                switchVisibility();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                toastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
                Log.e("retrofit error", t.toString());
                switchVisibility();
            }
        });
    }
    //
    private boolean isAlalHesabSizeValid(){
        ReadingConfigModel readingConfig= getValidReadingConfig();
        if(readingConfig==null){
            toastAndAlertBuilder.makeSimpleAlert("این فایل پیدا نشد");
            return false;
        }
        if(!readingConfig.isActive()){
            toastAndAlertBuilder.makeSimpleAlert("همکار گرامی تخلیه این فایل بدلیل فعال نبودن ممکن نیست ، لطفا در صورت " +
                    "تمایل از بخش تنظیمات آن را فعال نمایید");
            return false;
        }
        final float allowedRate=readingConfig.getAlalPercent();
        int _index=readingConfig.get_index();
        long alalHesabSize= statisticsRepo.getAlalHesabSize(_index);
        long unreadSize= statisticsRepo.getUnreadSize(_index);
        long kolSize= statisticsRepo.getKolSize(_index);
        float division=(((float)alalHesabSize)/kolSize);
        float myRate=allowedRate-division*100;
        if(myRate<0){
            return false;
        }
        if(unreadSize>0){
            toastAndAlertBuilder.makeSimpleAlert("تعداد"+" "+unreadSize+" "+" اشتراک قرائت نشده وجود دارد");
            return false;
        }
        return true;
    }
    //
    private boolean hasSpinnerValidItem(){
        int spinnerSelectedPosition=getSpinnerPosition();
        if(spinnerSelectedPosition<1){
            toastAndAlertBuilder.makeSimpleAlert("همکار گرامی لطفا ابتدا شماره لیست مورد نظر خود را انتخاب فرمایید");
            return false;
        }
        return true;
    }
    //
    private int getSpinnerPosition(){
        int spinnerSelectedPosition=listNumberSpinner.getSelectedItemPosition();
        return spinnerSelectedPosition;
    }
    //
    private String getSpinnerText(){
        String spinnerSelectedText=listNumberSpinner.getSelectedItem().toString();
        return spinnerSelectedText;
    }
    //
    private ReadingConfigModel getValidReadingConfig(){
        String _spinnerText=getSpinnerText();
        ReadingConfigModel readingConfig=readingConfigManager.get(_spinnerText);
        return readingConfig;
    }
    //
    private void doPostOffload(final ReadingConfigModel readingConfig){
        final List<OnOffLoadModel> onOffloads=onOffloadService.get(readingConfig.getTrackNumber());
        final List<CounterReadingReport> reports=counterReportService.get(readingConfig.getTrackNumber());
        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {
                if(onOffloads!=null && onOffloads.size()>0){
                    OnOffLoadModel.deleteInTx(onOffloads);
                }
                if(reports!=null && reports.size()>0){
                    CounterReadingReport.deleteInTx(reports);
                }
                ReadingConfigModel.delete(readingConfig);
                CapturedImageModel.deleteAll(CapturedImageModel.class);
            }
        });

    }
}
