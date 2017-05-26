package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.DifferentCompanyManager;
import com.sepehr.sa_sh.abfacounter01.IAbfaService;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.models.ChangePasswordModel;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangePasswordFragment extends Fragment {
    Button changePassButton;
    ProgressBar changePassProgressbar;
    EditText oldPass,newPass,confirmPass;
    String email,token;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_change_password, container, false);
        final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");
        initialize(rootView);
        onChangePassButtonClick();
        return rootView;
    }
    //
    private void initialize(View rootView){
        changePassButton=(Button)rootView.findViewById(R.id.changePasswordButton);
        changePassProgressbar=(ProgressBar)rootView.findViewById(R.id.progressbarChanging);
        oldPass=(EditText)rootView.findViewById(R.id.oldPass);
        newPass=(EditText)rootView.findViewById(R.id.newPass);
        confirmPass=(EditText)rootView.findViewById(R.id.confirmPass);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        email="m_"+prefs.getInt("userCode",0)+ DifferentCompanyManager.getEmailTail
                (DifferentCompanyManager.getActiveCompanyName());
        token=prefs.getString("token"," ");
    }
    //
    private void onChangePassButtonClick(){
        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchVisibility();
                String oldPassString,newPassString,confirmPassString;
                oldPassString=oldPass.getText().toString();
                newPassString=newPass.getText().toString();
                confirmPassString=confirmPass.getText().toString();
                if(!validateInputs(oldPassString,newPassString,confirmPassString)){
                    switchVisibility();
                    return;
                }
                else {
                    doChangePassword(email,oldPassString,newPassString,confirmPassString);
                }
            }
        });
    }

    private void switchVisibility(){
        if(changePassProgressbar.getVisibility()==View.GONE){
            changePassProgressbar.setVisibility(View.VISIBLE);
            changePassButton.setVisibility(View.GONE);
        }
        else {
            changePassProgressbar.setVisibility(View.GONE);
            changePassButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateInputs(String oldP,String newP,String confirmP){
        if(oldP.length()<1 || newP.length()<1 || confirmP.length()<1){
            showToast("لطفا تمامی فیلد ها را پر نمایید");
            return false;
        }
        if(!newP.equals(confirmP)){
            showToast("پسوورد جدید و تکرار آنها مطابقت ندارند");
            return false;
        }
        if(newP.length()<6){
            showToast("طول پسوورد انتخابی شما حداقل شش کاراکتر میباشد");
            return false;
        }
        return true;
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    private void doChangePassword(String email,String oldPass,String newPass,String confirmPass){
        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        ChangePasswordModel changePasswordModel=new ChangePasswordModel(email,oldPass,newPass,confirmPass);
        Call<String> call=abfaService.changePassword(token, changePasswordModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   retrofit2.Response<String> response) {
                int responseCode = response.code();
                String responseBody=response.body();
                String message = "";
                if (responseCode != 200) {
                    if (responseCode == 500) {
                        message = "خطای سرور";
                    } else if (responseCode == 404) {
                        message = "خطای آدرس";
                    } else if (responseCode == 401) {
                        message = "از ثبت شدن دستگاه در سامانه قرائت اطمینان حاصل کنید ، در صورت تکرار یوزر شما در لیست سیاه قرار خواهد گرفت";
                    }
                    Log.e("error:", message);
                }
                else {
                    if(responseBody.equals("200")){
                        message="تغییر پسوورد با موفقیت انجام شد ، لطفا پسوورد جدید خود را بخاطر بسپارید";
                    }
                    else if(responseBody.equals("401")){
                        message="از ثبت یوزر و دستگاه در سامانه قرائت کنتور اطمینان حاصل فرمایید ، در صورت تکرار یوزر شما در لیست سیاه قرار خواهد گرفت";
                    }
                    else if(responseBody.equals("400")){
                        message="عدم مطابقت پسوورد و تایید پسوورد ، در صورت تکرار یوزر شما بلک لیست خواهد شد";
                    }
                    else if(responseBody.equals("500")){
                        message="تغییر پسوورد به دلیل خطا انجام نپذیرفت";
                    }
                }
                AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                alert.setTitle("");
                alert.setMessage(message);
                alert.show();
                switchVisibility();
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
                switchVisibility();
            }
        });
    }

}
