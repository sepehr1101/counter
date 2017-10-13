package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.app.AlertDialog;
import android.content.Context;
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

import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.IAbfaService;
import com.sepehr.sa_sh.abfacounter01.LatLang;
import com.sepehr.sa_sh.abfacounter01.NetworkHelper;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.constants.SharedPrefStrings;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.QeireMojazModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by saeid on 10/1/2016.
 */
public class QeireMojazFragment extends DialogFragment {

    Context appContext;
    Button dissmiss,ok,captureImage;
    EditText preEshterak,postalCode,nextEshterak,tedadVahed;
    ProgressBar progressBar;
    TextView textViewSend;
    int userCode;
    String token,deviceId;
    BigDecimal trackNumber;
    IToastAndAlertBuilder toastAndAlertBuilder;
    ISharedPreferenceManager sharedPreferenceManager;

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
        appContext=getContext();
        sharedPreferenceManager=new SharedPreferenceManager(appContext);
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
        userCode=sharedPreferenceManager.getInt(SharedPrefStrings.USER_CODE);
        token=sharedPreferenceManager.getString(SharedPrefStrings.TOKEN);
        deviceId=Build.SERIAL;
        trackNumber=((DisplayViewPager)getActivity()).getCurrentTrackNumber();
        toastAndAlertBuilder=new ToastAndAlertBuilder(appContext);
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
        Integer tedadVahedInt;
        preEshterakText=preEshterak.getText().toString();
        postalCodeText=postalCode.getText().toString();
        nextEshterakText=nextEshterak.getText().toString();
        tedadVahedText=tedadVahed.getText().toString();
        tedadVahedInt=tedadVahedText.trim().length()<1?0:new Integer(tedadVahedText);
        LatLang latLang=((DisplayViewPager)getActivity()).getLatLang();
        QeireMojazModel qeireMojazModel=
                new QeireMojazModel(preEshterakText,nextEshterakText,
                        postalCodeText,latLang,tedadVahedInt,
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
        if(tedadVahedLenght>0 && tedadVahedLenght>20){
            toastAndAlertBuilder.makeSimpleAlert("لطفا تعداد واحد را اصلاح فرمایید");
            return false;
        }
        return true;
    }
    //
    private void resetUiElements(){
        ok.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
    //
    private void handleRetrofitResponseCode(int responseCode,String responseErrorMessage) {
        String errorMessage = SimpleErrorHandler.getErrorMessage(responseCode);
        Log.e("error:", errorMessage);
        toastAndAlertBuilder.makeSimpleAlert(errorMessage+"\n"+responseErrorMessage);
    }
    //
    private void send(QeireMojazModel model) {
        progressBar.setVisibility(View.VISIBLE);
        textViewSend.setVisibility(View.VISIBLE);
        ok.setEnabled(false);
        IAbfaService abfaService = NetworkHelper.getInstance(false).create(IAbfaService.class);
        Call<String> call=abfaService.sendQeireMojaz(token, model, deviceId, userCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   retrofit2.Response<String> response) {
                if (!response.isSuccessful()) {
                    SimpleErrorHandler.APIError error=SimpleErrorHandler.parseError(response);
                    handleRetrofitResponseCode(response.code(),error.message());
                    resetUiElements();
                    return;
                }
                String result = response.body();
                progressBar.setVisibility(View.GONE);
                textViewSend.setText("با موفقیت ارسال شد");
                Log.e("qeierMojaz result",result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                toastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
                Log.e("retrofit error", t.toString());
               resetUiElements();
            }
        });
    }

}
