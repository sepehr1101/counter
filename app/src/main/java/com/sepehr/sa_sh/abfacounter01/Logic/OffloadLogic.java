package com.sepehr.sa_sh.abfacounter01.Logic;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;
import com.orm.SugarTransactionHelper;
import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.CounterReadingReport;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CapturedImagesService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICapturedImagesService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IOnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IStatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DeviceSerialManager;
import com.sepehr.sa_sh.abfacounter01.DifferentCompanyManager;
import com.sepehr.sa_sh.abfacounter01.IAbfaService;
import com.sepehr.sa_sh.abfacounter01.NetworkHelper;
import com.sepehr.sa_sh.abfacounter01.Output;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IImageOrVideoManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ImageOrVideoManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by _1101 on 5/7/2017.
 */

public class OffloadLogic implements IOffloadLogic{
    Context mContext;
    Button mStartButton;
    ProgressBar mProgressBar;
    TextView mStateTextView;
    Spinner mSpinner;
    CheckBox mForceImageOffLoad;

    int userCode;
    String token;
    String deviceId;

    IReadingConfigService readingConfigService;
    IStatisticsRepo statisticsRepo;
    IOnOffloadService onOffloadService;
    ICounterReportService counterReportService;
    IToastAndAlertBuilder toastAndAlertBuilder;

    ArrayAdapter<String> adapter;
    List<String> listNumbers;
    IImageOrVideoManager imageOrVideoManager;
    ICapturedImagesService capturedImagesService;

    public OffloadLogic(Context mContext,
                        Button mStartButton,
                        ProgressBar mProgressBar,
                        TextView mStateTextView,
                        Spinner listNumberSpinner,
                        int userCode, String token,
                        String deviceId,
                        IReadingConfigService readingConfigService,
                        IToastAndAlertBuilder toastAndAlertBuilder,
                        IStatisticsRepo statisticsRepo,
                        IOnOffloadService onOffloadService,
                        ICounterReportService counterReportService,
                        CheckBox forceImageOffLoad) {
        this.mContext = mContext;
        this.mStartButton =(Button) mStartButton;
        this.mProgressBar = mProgressBar;
        this.mStateTextView = mStateTextView;
        this.mSpinner=listNumberSpinner;
        this.mForceImageOffLoad=forceImageOffLoad;

        this.userCode = userCode;
        this.token = token;
        this.deviceId = deviceId;

        this.readingConfigService = readingConfigService;
        this.statisticsRepo=statisticsRepo;
        this.onOffloadService=onOffloadService;
        this.counterReportService=counterReportService;
        this.toastAndAlertBuilder = toastAndAlertBuilder;
        this.imageOrVideoManager= new ImageOrVideoManager(mContext,"savedMedia");
        this.capturedImagesService= new CapturedImagesService();
        //
        listNumbers= readingConfigService.getListNumbers();
        adapter= new ArrayAdapter<String>(mContext,R.layout.spinner_custom_item, listNumbers);
        //adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        mSpinner.setAdapter(adapter);
    }

    private void switchVisibility(){
        if(mStartButton.getVisibility()== View.VISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mStateTextView.setVisibility(View.VISIBLE);
            mStartButton.setEnabled(false);
            mStartButton.setVisibility(View.GONE);
        }
        else if(mStartButton.getVisibility()==View.GONE){
            mProgressBar.setVisibility(View.GONE);
            mStateTextView.setVisibility(View.GONE);
            mStartButton.setEnabled(true);
            mStartButton.setVisibility(View.VISIBLE);
        }
    }
    private void startProgress(){
        mProgressBar.setVisibility(View.VISIBLE);
        mStateTextView.setVisibility(View.VISIBLE);
        mStartButton.setEnabled(false);
        mStartButton.setVisibility(View.GONE);
    }
    private void stopProgress(){
        mProgressBar.setVisibility(View.GONE);
        mStateTextView.setVisibility(View.GONE);
        mStartButton.setEnabled(true);
        mStartButton.setVisibility(View.VISIBLE);
    }

    public void start(boolean isLocal) {
        try {
            if (!hasSpinnerValidItem()) {
                return;
            }
            final ReadingConfigModel readingConfig = getValidReadingConfig();
            startProgress();
            //switchVisibility();
            if(mForceImageOffLoad.isChecked()){
                offloadUnsendedImages(readingConfig,isLocal);
            }
            else {
                boolean isAlalHesabSizeValid = isAlalHesabSizeValid();
                if (!isAlalHesabSizeValid) {
                    String errorMessage =  "درصد علی الحساب و خوانده نشده بالاتر ازحد مجاز";
                    toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                    stopProgress();
                    //switchVisibility();
                    return;
                }
                doOffLoadCounterReadingTable(isLocal,readingConfig);
            }
        } catch (Exception e) {
            Log.e("offload error", e.getMessage());
            toastAndAlertBuilder.makeSimpleAlert("خطا حین تخلیه اطلاعات");
            stopProgress();
        }
    }

    private void doOffLoadCounterReadingTable(boolean isLocal, final ReadingConfigModel readingConfig) {
        final List<OnOffLoadModel> onOffloads=
                onOffloadService.getNeedToBeOffloadedOverall(readingConfig.get_index());

        final List<CounterReadingReport> readingReports=
                counterReportService.getNeedToBeOffloadedOverall(readingConfig.getTrackNumber());

        Output output=new Output(readingReports,onOffloads);
        IAbfaService abfaService = NetworkHelper.getInstance(isLocal).create(IAbfaService.class);
        Call<Integer> call=abfaService.sendCounterReadingInfo(token,output,deviceId,userCode,true,readingConfig.getTrackNumber());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call,retrofit2.Response<Integer> response) {
                Integer count = response.body();
                int responseCode = response.code();
                if (responseCode != 200) {
                    handleRetrofitResponseCode(responseCode);
                    stopProgress();
                    //switchVisibility();
                    return;
                }

                doPostOffload(readingConfig);
                if (count == 0) {
                    mStateTextView.setText("قبلا اطلاعات تخلیه شده ، دستگاه شما آماده بارگیری اطلاعات جدید میباشد");
                    toastAndAlertBuilder.makeSimpleAlert("تخلیه انجام شد ، همکار گرامی خدا قوت");
                } else {
                    mStateTextView.setText("تخلیه به اتمام رسید" + " " + "تعداد" + " " +
                            count.toString() + " " + "رکورد تخلیه شد");
                }
                stopProgress();
                //switchVisibility();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                toastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
                Log.e("retrofit error", t.toString());
                stopProgress();
                //switchVisibility();
            }
        });
    }
    //
    private void handleRetrofitResponseCode(int responseCode) {
        String errorMessage = SimpleErrorHandler.getErrorMessage(responseCode);
        Log.e("error:", errorMessage);
        toastAndAlertBuilder.makeSimpleAlert(errorMessage);
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
        int spinnerSelectedPosition=mSpinner.getSelectedItemPosition();
        return spinnerSelectedPosition;
    }
    //
    private String getSpinnerText(){
        String spinnerSelectedText=mSpinner.getSelectedItem().toString();
        return spinnerSelectedText;
    }
    //
    private ReadingConfigModel getValidReadingConfig(){
        String _spinnerText=getSpinnerText();
        ReadingConfigModel readingConfig=readingConfigService.get(_spinnerText);
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
                OnOffLoadModel.deleteAll(OnOffLoadModel.class,"TRACK_NUMBER=?",readingConfig.getTrackNumber()+"");
                CounterReadingReport.deleteAll(CounterReadingReport.class,"TRACK_NUMBER=?",readingConfig.getTrackNumber()+"");
                if(reports!=null && reports.size()>0){
                    CounterReadingReport.deleteInTx(reports);
                }
                ReadingConfigModel.delete(readingConfig);
                CapturedImageModel.deleteAll(CapturedImageModel.class);
            }
        });

    }

    private void offloadUnsendedImages(final ReadingConfigModel readingConfig,boolean isLocal){
        List<CapturedImageModel> capturedImageModelList=
                capturedImagesService.getCapturedImages(0,readingConfig.getTrackNumber().toString());
        if(capturedImageModelList.size()>0){
            sendImage(capturedImageModelList,0,isLocal,readingConfig);
        }else {
            boolean isAlalHesabSizeValid = isAlalHesabSizeValid();
            if (!isAlalHesabSizeValid) {
                String errorMessage = "درصد علی الحساب و خوانده نشده بالاتر ازحد مجاز";
                toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                stopProgress();
                return;
            }
            doOffLoadCounterReadingTable(isLocal,readingConfig);
        }
    }
    private void sendImage(final List<CapturedImageModel> capturedImageModelList, final int index,
                           final boolean isLocal,final ReadingConfigModel readingConfig ){
        int size=capturedImageModelList.size();
        if(index>size-1){
            boolean isAlalHesabSizeValid = isAlalHesabSizeValid();
            if (!isAlalHesabSizeValid) {
                String errorMessage = "درصد علی الحساب و خوانده نشده بالاتر ازحد مجاز";
                if(capturedImageModelList.size()>0) {
                    errorMessage = "درصد علی الحساب و خوانده نشده بالاتر ازحد مجاز. عکس ها با موفقیت تخلیه شد";
                }
                toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                stopProgress();
                return;
            }
            doOffLoadCounterReadingTable(isLocal,readingConfig);
            return;
        }
        final CapturedImageModel imageModel=capturedImageModelList.get(index);
        File originalImage=new File(imageModel.getPath());
        final File fileToUpload = imageOrVideoManager.downsizeImage(originalImage,75);
        Ion.with(mContext)
                .load(DifferentCompanyManager.getCameraUploadUrl
                        (DifferentCompanyManager.getActiveCompanyName()))
                .setHeader("Authorization", token)
                .setLogging("offload log", Log.DEBUG)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        Log.e("Image offload","on progress");

                       //switchVisibility();
                    }
                })
                .setTimeout(1000*60)
                .setMultipartFile("file","image/png",fileToUpload)
                .setMultipartParameter("userCode",userCode+"")
                .setMultipartParameter("deviceId", DeviceSerialManager.getSerial(mContext))
                .setMultipartParameter("billId",imageModel.getBillId().trim())
                .setMultipartParameter("trackNumber",imageModel.getTrackNumber().toString())
                .asString()
                .withResponse()
                // run a callback on completion                .
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        //switchVisibility();
                        if (e != null) {
                            stopProgress();
                            toastAndAlertBuilder.makeSimpleAlert("خطا حین ارسال تصاویر"+"\n"+"متن خطا:"+ e.getMessage());
                            if(e.getMessage()!=null &&
                                    !e.getMessage().equals(null) &&
                                    e.getMessage().length()>0)
                            {
                                Log.e("MyLog ", e.getMessage());
                            }
                            return;
                        }
                        int responseCode=result.getHeaders().code();
                        if(responseCode!=200){
                            String message= SimpleErrorHandler.getErrorMessage(responseCode);
                            toastAndAlertBuilder.makeSimpleAlert("خطا حین ارسال تصاویر"+"\n"+"متن خطا:"+ message);
                            stopProgress();
                            return;
                        }
                        imageModel.setStatus(1);
                        imageModel.save();
                        //switchVisibility();
                        sendImage(capturedImageModelList,index+1,isLocal,readingConfig);
                    }
                });
    }
}
