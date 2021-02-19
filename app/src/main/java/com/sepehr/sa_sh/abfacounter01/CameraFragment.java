package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.future.ResponseFuture;
import com.orm.SugarTransactionHelper;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.OnOffLoadStatic;
import com.sepehr.sa_sh.abfacounter01.Fragments.MobileReport;
import com.sepehr.sa_sh.abfacounter01.constants.ActivityRequestCode;
import com.sepehr.sa_sh.abfacounter01.constants.CompanyNames;
import com.sepehr.sa_sh.abfacounter01.constants.ImageScale;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IImageOrVideoManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ImageOrVideoManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Mr sepehr :) on 7/26/2016.
 */
public class CameraFragment extends DialogFragment {
    //
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;
    private final int RESULT_OK=getActivity().RESULT_OK;
    private final int RESULT_CANCELED=getActivity().RESULT_CANCELED;
    private CapturedImageModel imageModel;
    //
    int userCode;
    String bill_id,token,onOffloadId;
    BigDecimal trackNumber;
    boolean allowTakePicture=true;
    Context appContext;
    IImageOrVideoManager imageOrVideoManager;
    //
    TextView textViewUpload;
    ProgressBar progressUpload;
    Button buttonUpload;
    ReadingConfigModel readingConfig;
    IToastAndAlertBuilder toastAndAlertBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        initialize(rootView);
        setRetainInstance(true);
        bill_id=((DisplayViewPager)getActivity()).getBill_id();
        trackNumber=((DisplayViewPager)getActivity()).getCurrentTrackNumber();
        onOffloadId=((DisplayViewPager)getActivity()).getId();
        List<CapturedImageModel> capturedImages=CapturedImageModel
                .find(CapturedImageModel.class, "O_ID= ? ", onOffloadId);
       /* List<OnOffLoadModel> currentOffLoadStates=OnOffLoadModel
                .find(OnOffLoadModel.class, "BILL_ID= ? ", bill_id);*/
        OnOffLoadModel tmp = OnOffLoadModel
                .find(OnOffLoadModel.class, "SID = ? ", onOffloadId)
                .get(0);
        //OnOffLoadModel tmp=currentOffLoadStates.get(0);
        Integer currentOffLoadState=tmp.offLoadState;
        if( currentOffLoadState==null){
            String pleaseSetStateFirst= "لطفا ابتدا وضعیت قرائت این مشترک را مشخص فرمایید";
            toastAndAlertBuilder.makeSimpleToast(pleaseSetStateFirst);
            allowTakePicture=false;
            toggleEnable(false,false);
            textViewUpload.setText(pleaseSetStateFirst);
        }

        if(capturedImages.size()>0){
            allowTakePicture=false;
            toggleEnable(false,false);
            previewCapturedImage(capturedImages.get(0).getPath());
        }
        // Checking camera availability
        if (!imageOrVideoManager.isDeviceSupportCamera()) {
            String yourDeviceNotSupport= "متاسفانه دستگاه شما از دوربین پشتیبانی نمی کند";
            toastAndAlertBuilder.makeSimpleToast(yourDeviceNotSupport);
            // will close the dialog if the device does't have camera
            allowTakePicture=false;
            toggleEnable(false,false);
            textViewUpload.setText(yourDeviceNotSupport);
        }
        if(!amIAllowedToCaptureMore()){
            allowTakePicture=false;
            toggleEnable(false,false);
            textViewUpload.setText("به دلیل محدودیت ترافیک اینترنت لطفا از گرفتن عکس های جدید خودداری فرمایید");
        }

        //
        dismissDialog(rootView);
        return rootView;
    }
    //
    private void dismissDialog(View rootView){
        Button dismiss = (Button) rootView.findViewById(R.id.dismissCameraCapture);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DifferentCompanyManager.getActiveCompanyName() == CompanyNames.KERMANSHAH) {
                    FragmentManager fmEsf = ((DisplayViewPager) getActivity()).getSupportFragmentManager();
                    MobileReport dialogFragment = new MobileReport();
                    dialogFragment.show(fmEsf, "گزارش کنتور");
                }
                dismiss();
            }
        });
    }

    private void initialize(View rootView){
        appContext=getContext();
        imgPreview = (ImageView)rootView.findViewById(R.id.imageHolder);
        videoPreview = (VideoView)rootView.findViewById(R.id.videoHolder);
        btnCapturePicture = (Button)rootView.findViewById(R.id.captureImageButton);
        btnRecordVideo = (Button)rootView.findViewById(R.id.recordVideoButton);

        progressUpload=(ProgressBar)rootView.findViewById(R.id.progressBarImageUpload);
        textViewUpload=(TextView)rootView.findViewById(R.id.textViewImageUpload);
        buttonUpload=(Button)rootView.findViewById(R.id.buttonUploadImage);
        progressUpload.setScaleY(7f);
        //
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        userCode=prefs.getInt("userCode", 0);
        token=prefs.getString("token", " ");
        readingConfig = ReadingConfigModel.first(ReadingConfigModel.class);
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        imageOrVideoManager =new ImageOrVideoManager(appContext,"savedMedia");
        setSomeClickListeners();
    }
    //
    private void setSomeClickListeners(){
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        //
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordVideo();
            }
        });
        //
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendImage();
                } catch (Exception e) {
                    if(e.getMessage()!=null &&
                       !e.getMessage().equals(null) &&
                       e.getMessage().length()>0)
                    {
                        Log.e("fail ", e.getMessage());
                    }
                    toastAndAlertBuilder.makeSimpleToast("خطا حین ارسال");
                }

            }
        });
    }
    //
    @Override
    public  void onResume(){
        //getDialog().getWindow().setLayout(1400, 1400);
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == ActivityRequestCode.CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                toastAndAlertBuilder.makeSimpleToast("گرفتن عکس توسط شما کنسل شد");
            } else {
                // failed to capture image
                toastAndAlertBuilder.makeSimpleToast("متاسفانه گرفتن عکس میسر نشد");
            }
        } else if (requestCode == ActivityRequestCode.VIDEO_RECORD) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                toastAndAlertBuilder.makeSimpleToast("رکورد ویدئو توسط شما کنسل شد");
            } else {
                // failed to record video
                toastAndAlertBuilder.makeSimpleToast("متاسفانه رکورد ویدئو میسر نشد");
            }
        }
    }
    //
    private void captureImage() {
        Intent intent = imageOrVideoManager.getCaptureImageIntent();
        startActivityForResult(intent,ActivityRequestCode.CAMERA_CAPTURE);
    }
    //
    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            String filePath= imageOrVideoManager.getFilePath();
            imageModel=new CapturedImageModel(bill_id,userCode,filePath,trackNumber,0,onOffloadId);
            final OnOffLoadModel currentCounterReadingModel01= OnOffLoadStatic.GetOnOfLoad(bill_id);
            SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                @Override
                public void manipulateInTransaction() {
                    imageModel.save();
                    currentCounterReadingModel01.cameraState = 1;
                    currentCounterReadingModel01.save();
                }
            });

            // hide video preview
            toggleVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE);
            final Bitmap bitmap = imageOrVideoManager.getBitmap(filePath, ImageScale.XX_SMALL);
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
            toastAndAlertBuilder.makeSimpleAlert(e.getMessage());
        }
    }
    //
    private void previewCapturedImage(String filePath){
        try {
            // hide video preview
            toggleVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
            final Bitmap bitmap = imageOrVideoManager.getBitmap(filePath,ImageScale.AS_IS);
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
            toastAndAlertBuilder.makeSimpleAlert(e.getMessage());
        }
    }
    /*
     * Recording video
     */
    private void recordVideo() {
        Intent intent = imageOrVideoManager.getRecordVidoeIntent();
        // start the video capture Intent
        startActivityForResult(intent, ActivityRequestCode.VIDEO_RECORD);
    }
    //
    /*
     * Previewing recorded video
     */
    private void previewVideo() {
        try {
            // hide image preview
            toggleVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
            videoPreview.setVideoPath(imageOrVideoManager.getFilePath());
            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    //region ________________________________________ Helper Mehtods ________________________________________

    private void sendImage() {
        buttonUpload.setVisibility(View.GONE);
        File originalImage=new File(imageOrVideoManager.getFilePath());
        int scaleInPercent=75;
        if(DifferentCompanyManager.getActiveCompanyName()==CompanyNames.KERMANSHAH){
            scaleInPercent=65;
        }
        final File fileToUpload = imageOrVideoManager.downsizeImage(originalImage,75);
        Ion.with(getContext())
                .load(DifferentCompanyManager.getCameraUploadUrl
                        (DifferentCompanyManager.getActiveCompanyName()))
                .setHeader("Authorization", token)
                .setLogging("MyLogs", Log.DEBUG)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        toggleVisibility(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
                       // progressUpload.setVisibility(View.VISIBLE);
                        textViewUpload.setVisibility(View.VISIBLE);
                        int mProgress  = (int) (100*uploaded / total);
                        progressUpload.setProgress(mProgress);
                    }
                })
                .setTimeout(1000*60)
                .setMultipartFile("file","image/png",fileToUpload)
                .setMultipartParameter("userCode",userCode+"")
                .setMultipartParameter("deviceId",DeviceSerialManager.getSerial(appContext))
                .setMultipartParameter("billId",bill_id.trim())
                .setMultipartParameter("trackNumber",trackNumber.toString())
                .setMultipartParameter("id",onOffloadId.trim())
                .asString()
                .withResponse()
                        // run a callback on completion                .
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        progressUpload.setVisibility(View.GONE);
                        if (e != null) {
                            textViewUpload.setText("ارسال با خطا مواجه شد");
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
                            toastAndAlertBuilder.makeSimpleAlert("عکس با موفقیت در دستگاه شما ذخیره شد اما به دلیل خطا ارسال نشد"+
                                    "\n"+"متن خطا:"+message);
                            return;
                        }
                        //result=result==null?"":result;
                        //Log.e("MyLog", "ion success" + result);
                        String remainedCount = "به دلیل محدودیت ترافیک اینترنت قادر به گرفتن فقط" + " "
                                + getRemindedPicture()
                                + " عکس دیگر هستید";
                        textViewUpload.setText("ارسال عکس با موفقیت انجام شد" + "\n" + remainedCount);
                        imageModel.setStatus(1);
                        imageModel.save();
                        toggleEnable(false, false);
                    }
                });

    }
    //
    private void toggleEnable(boolean allowTakePicture,boolean allowRecordVideo){
        btnCapturePicture.setEnabled(allowTakePicture);
        btnRecordVideo.setEnabled(allowRecordVideo);
    }
    //
    private void toggleVisibility(int imageViewVisibility,int videoViewVisibility,
                               int uploadButtonVisibility,int progressbarVisibility){
        imgPreview.setVisibility(imageViewVisibility);
        videoPreview.setVisibility(videoViewVisibility);
        buttonUpload.setVisibility(uploadButtonVisibility);
        progressUpload.setVisibility(progressbarVisibility);
    }
    //
    private boolean amIAllowedToCaptureMore() {
        int maxAllowdAmount = convertMaxCaptureRateToAmount();
        long capturedCount = CapturedImageModel.count(CapturedImageModel.class);
        if (capturedCount < maxAllowdAmount) {
            return true;
        }
        return false;
    }
    //
    private int convertMaxCaptureRateToAmount(){
        /*    maxRate/100=x/overallCount  then compare x to already captured*/
        ReadingConfigModel readingConfig= ReadingConfigModel.first(ReadingConfigModel.class);
        int maxCaptureRate=readingConfig.getImagePercent();
        long overallCount=OnOffLoadModel.count(OnOffLoadModel.class);
        int capturedRate=Math.round(overallCount * ((float) maxCaptureRate / 100));
        return capturedRate;
    }
    //
    private int getRemindedPicture(){
        int maxAmount=convertMaxCaptureRateToAmount();
        long capturedCount = CapturedImageModel.count(CapturedImageModel.class);
        int reminded=maxAmount-(int)capturedCount;
        return reminded-1;
    }
    //endregion
}
