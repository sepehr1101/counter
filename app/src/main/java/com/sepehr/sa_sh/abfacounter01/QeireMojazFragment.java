package com.sepehr.sa_sh.abfacounter01;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by saeid on 10/1/2016.
 */
public class QeireMojazFragment extends DialogFragment {

    Button dissmiss,ok,captureImage;
    EditText preEshterak,postalCode,nextEshterak,tedadVahed;
    ProgressBar progressBar;
    TextView textViewSend;
    int userCode;
    String token,deviceId;
    BigDecimal trackNumber;
    IToastAndAlertBuilder toastAndAlertBuilder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_qeire_mojaz, container, false);
        initialize(rootView);
        dismissDialog();
        submit();
        return rootView;
    }
    //
    private void initialize(View rootView){
        dissmiss = (Button) rootView.findViewById(R.id.dismiss);
        ok=(Button)rootView.findViewById(R.id.ok);
        captureImage=(Button)rootView.findViewById(R.id.captureImage);
        preEshterak=(EditText)rootView.findViewById(R.id.preEshterak);
        postalCode=(EditText)rootView.findViewById(R.id.postalCode);
        nextEshterak=(EditText)rootView.findViewById(R.id.nextEshterak);
        tedadVahed=(EditText)rootView.findViewById(R.id.tedadVahed);
        progressBar=(ProgressBar)rootView.findViewById(R.id.qeireMojazProgress);
        textViewSend=(TextView)rootView.findViewById(R.id.qeireMojazText);
        textViewSend.setText("در حال ارسال");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userCode=prefs.getInt("userCode", 0);
        token=prefs.getString("token", " ");
        deviceId=Build.SERIAL;
        trackNumber=((DisplayViewPager)getActivity()).getCurrentTrackNumber();
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
    }
    //
    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
    //
    private void dismissDialog(){
        dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //
    private void submit(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){
                    doSubmit();
                }
            }
        });
    }
    //
    private void doSubmit(){
        String preEshterakText,postalCodeText,nextEshterakText,tedadVahedText;
        preEshterakText=preEshterak.getText().toString();
        postalCodeText=postalCode.getText().toString();
        nextEshterakText=nextEshterak.getText().toString();
        tedadVahedText=tedadVahed.getText().toString();
        LatLang latLang=((DisplayViewPager)getActivity()).getLatLang();
        QeireMojazModel qeireMojazModel=
                new QeireMojazModel(preEshterakText,nextEshterakText,
                        postalCodeText,latLang,new Integer(tedadVahedText),
                        "img_path",1,trackNumber);
        qeireMojazModel.save();
        send(qeireMojazModel);
    }
    //
    private boolean isValidated(){
        int preEshtereakLength=preEshterak.getText().length();
        int postalCodeLength=postalCode.getText().length();
        int nextEshterakLenght=nextEshterak.getText().length();
        int tedadVahedLenght=tedadVahed.getText().length();
        if(preEshtereakLength<1 && postalCodeLength<1 && nextEshterakLenght<1 && tedadVahedLenght<1 ){
            toastAndAlertBuilder.makeSimpleToast("لطفا دست کم یکی از موارد گزارش غیر مجاز را وارد فرمایید");
            return false;
        }
        if(preEshtereakLength>0 && preEshtereakLength>10){
            toastAndAlertBuilder.makeSimpleAlert("حداکثر طول اشتراک 10 کاراکتر میباشد");
            return false;
        }
        if(nextEshterakLenght>0 && nextEshterakLenght>10){
            toastAndAlertBuilder.makeSimpleAlert("حداکثر طول اشتراک 10 کاراکتر میباشد");
            return false;
        }
        if(tedadVahedLenght>0 && tedadVahedLenght>4){
            toastAndAlertBuilder.makeSimpleAlert("لطفا تعداد واحد را اصلاح فرمایید");
            return false;
        }
        return true;
    }
    //
    private void send(QeireMojazModel model) {
        progressBar.setVisibility(View.VISIBLE);
        textViewSend.setVisibility(View.VISIBLE);
        ok.setEnabled(false);
        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        Call<String> call=abfaService.sendQeireMojaz(token, model, deviceId, userCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   retrofit2.Response<String> response) {
                int responseCode = response.code();
                String errorMessage = "";
                if (responseCode != 200) {
                    if (responseCode == 500) {
                        errorMessage = "خطای سرور";
                    } else if (responseCode == 404) {
                        errorMessage = "خطای آدرس";
                    } else if (responseCode == 401) {
                        errorMessage = "همکار گرامی ، از ثبت دستگاه خود در سامانه قرائت اطمینان حاصل فرمایید";
                    }
                    Log.e("error:", errorMessage);
                    AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                    alert.setTitle("");
                    alert.setMessage(errorMessage);
                    alert.show();
                    ok.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                String  result = response.body();
                progressBar.setVisibility(View.GONE);
                textViewSend.setText("ارسال شد");
                Log.e("qeierMojaz result",result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String errorMessage = "";
                if (t instanceof SocketTimeoutException) {
                    errorMessage = "خطای شبکه ،لطفا از اتصال اینترنت اطمینان حاصل فرمایید";
                } else if (t instanceof IOException) {
                    errorMessage = "خطای ارتباطی شبکه";
                } else {
                    errorMessage = "خطا، لطفا مراتب را از اشکال ایجاد شده مطلع فرمایید";
                }
                AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                alert.setTitle("");
                alert.setMessage(errorMessage);
                alert.show();
                Log.e("retrofit error", t.toString());
                ok.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    //
    //region x
     /*

     ok.setVisibility(View.INVISIBLE);
        //File originalImage=new File(fileUri.getPath());
        //final File fileToUpload = downsizeImage(originalImage);
        Ion.with(getContext())
                .load("http://api.kontori.ir/api/QeireMojaz")
                .setLogging("MyLogs", Log.DEBUG)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        progressBar.setVisibility(View.VISIBLE);
                        textViewSend.setVisibility(View.VISIBLE);
                        int mProgress = (int) (100 * uploaded / total);
                        progressBar.setProgress(mProgress);
                    }
                })
                .setTimeout(1000 * 60)
                //.setMultipartFile("file","image/png",fileToUpload)
                .setBodyParameter("userCode", userCode + "")
                .setBodyParameter("deviceId", Build.SERIAL)
                .setBodyParameter("latitude", model.Latitude.toString())
                .setBodyParameter("longitude", model.Longitude.toString())
                .setBodyParameter("preEshteak", model.preEshterak)
                .setBodyParameter("postalCode", model.postalCode)
                .setBodyParameter("nextEshterak", model.nextEshterak)
                .setBodyParameter("tedadVahed", model.tedadVahed.toString())
                        //.asJsonObject()
                .asString()
                        // run a callback on completion                .
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("MyLogs", result);
                        if (e != null) {
                            //  Toast.makeText(context, "Error uploading file", Toast.LENGTH_LONG).show();
                            textViewSend.setText("ارسال با خطا مواجه شد");
                            Log.e("MyLog", e.getMessage());
                            return;
                        }
                      /*  Log.e("MyLog", "ion success" + result);
                        String remainedCount = "به دلیل محدودیت ترافیک اینترنت قادر به گرفتن فقط" + " "
                                + (MAX_CAPTURED_LIMIT - 1 - capturedCount)
                                + " عکس دیگر هستید";
                        textViewUpload.setText("ارسال عکس با موفقیت انجام شد" + "\n" + remainedCount);*/
   /* if(result.equals("saved")){
        textViewSend.setText("انجام شد");
    }
    captureImage.setEnabled(false);
}
});*/

    //endregion
}
