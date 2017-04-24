package com.sepehr.sa_sh.abfacounter01;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;
import com.sepehr.sa_sh.abfacounter01.models.SpecialLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    String token,deviceId;
    int userCode;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_special_load, container, false);
        initialize(rootView);
        onButtonSpecialLoadClick();
        return rootView;
    }
    //
    private void initialize(View rootView){
        face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");
        progressSpecialLoad=(ProgressBar)rootView.findViewById(R.id.progressbarSpecialLoad);
        buttonSpecialLoad=(Button)rootView.findViewById(R.id.specialOffLoadButton);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        userCode=prefs.getInt("userCode",0);
        token=prefs.getString("token"," ");
    }
    //
    private void switchVisibility(){
        if(progressSpecialLoad.getVisibility()==View.GONE){
            progressSpecialLoad.setVisibility(View.VISIBLE);
            buttonSpecialLoad.setVisibility(View.GONE);
        }
        else{
            progressSpecialLoad.setVisibility(View.GONE);
            buttonSpecialLoad.setVisibility(View.VISIBLE);
        }
    }
    //
    private void onButtonSpecialLoadClick(){
        //token="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1laWQiOiI5YWMzZDkwNy1kMDJiLTQwZDMtODc3NS0zMmQyNjdjMzA3NDUiLCJ1bmlxdWVfbmFtZSI6Im1fNDJAc3d0LmlyIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS9hY2Nlc3Njb250cm9sc2VydmljZS8yMDEwLzA3L2NsYWltcy9pZGVudGl0eXByb3ZpZGVyIjoiQVNQLk5FVCBJZGVudGl0eSIsIkFzcE5ldC5JZGVudGl0eS5TZWN1cml0eVN0YW1wIjoiNTcwODc2ZGYtZmZhYy00NDExLWIyODgtMTNlZTViY2JiZGE1Iiwicm9sZSI6IkNvdW50ZXJSZWFkZXIiLCJ6b25lSWQiOiI2OCIsImFjdGlvbiI6InJlYWRfZGV0YWlsX3JlcG9ydCIsIkZURSI6IjAiLCJ1c2VyQ29kZSI6IjE0MiIsImlzcyI6Imh0dHA6Ly84MS45MC4xNDguMjUiLCJhdWQiOiI0MTRlMTkyN2EzODg0ZjY4YWJjNzlmNzI4MzgzN2ZkMSIsImV4cCI6MTQ3OTYyNTY0OCwibmJmIjoxNDc4NzYxNjQ4fQ.W90bSCP8ojX--gwOaSnQbhBktQsyHKtBfYqCixoftt4";
        deviceId=Build.SERIAL;
        //deviceId="5203ca1bb46c73b7";
        buttonSpecialLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchVisibility();
                IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
                Call<List<SpecialLoadModel>> call=
                        abfaService.getMySpecialWorks(token, deviceId);
                call.enqueue(new Callback<List<SpecialLoadModel>>() {
                    @Override
                    public void onResponse(Call<List<SpecialLoadModel>> call,
                                           retrofit2.Response<List<SpecialLoadModel>> response) {
                        int responseCode = response.code();
                        String errorMessage = "";
                        if (responseCode != 200) {
                            if (responseCode == 500) {
                                errorMessage = "خطای سرور";
                            } else if (responseCode == 404) {
                                errorMessage = "خطای آدرس";
                            } else if (responseCode == 401) {
                                errorMessage = "از ثبت شدن دستگاه در سامانه قرائت اطمینان حاصل کنید ، در صورت تکرار یوزر شما در لیست سیاه قرار خواهد گرفت";
                            }
                            else if(responseCode==400){
                                errorMessage="بارگیری ویژه برای شما به ثبت نرسیده ، در صورتی که اطلاعات قرائت شما از دست رفته با راهبر سیستم تماس حاصل فرمایید";
                            }
                            Log.e("error:", errorMessage);
                            AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                            alert.setTitle("");
                            alert.setMessage(errorMessage);
                            alert.show();
                            switchVisibility();
                            return;
                        }
                        List<SpecialLoadModel> specialLoadList=response.body();
                        int count=specialLoadList.size();
                        Log.e("count :", count + "");
                        int i=0;
                        final List<CounterReadingModel01> counterReadingModel01List=new ArrayList<>();
                        HashMap<Integer, BigDecimal> inverseHashMap = CounterStateHash.getHMapInverse();

                        for (SpecialLoadModel loadModel : specialLoadList) {
                            BigDecimal counterState= null;
                            if(loadModel.getCounterStateCode()!=null){
                              counterState=inverseHashMap.get(Integer.parseInt(loadModel.getCounterStateCode().toString()));
                            }
                            CounterReadingModel01 readingModel01=new CounterReadingModel01(loadModel,i,counterState);
                            counterReadingModel01List.add(readingModel01);
                            i++;
                        }
                        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                            @Override
                            public void manipulateInTransaction() {
                                OnOffLoadModel.deleteAll(OnOffLoadModel.class);
                                CapturedImageModel.deleteAll(CapturedImageModel.class);
                                CounterReadingReport.deleteAll(CounterReadingReport.class);
                                SugarRecord.saveInTx(counterReadingModel01List);
                                Log.i("save done", " 2");
                            }
                        });
                        AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                        alert.setTitle("");
                        alert.setMessage("بارگیری ویژه به اتمام رسید ، اطلاعات قبلی با موفقیت بازیابی شد");
                        alert.show();
                        switchVisibility();
                    }//callback onResponse

                    @Override
                    public void onFailure(Call<List<SpecialLoadModel>> call, Throwable t) {
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
                        switchVisibility();
                    }//onFailure
                });
            }
        });
    }
}
