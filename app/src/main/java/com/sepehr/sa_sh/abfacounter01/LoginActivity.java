package com.sepehr.sa_sh.abfacounter01;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import java.lang.reflect.Method;
import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {
    IToastAndAlertBuilder toastAndAlertBuilder;
    ISharedPreferenceManager sharedPreferenceManager;
    Button loginButton;
    EditText usercodeEditCode,passwordEditCode;
    TextView applicationVersion,myVersionIs;
    String userCode,password,appVersion;
    Typeface face;
    int failureCount=0;
    final int ALLOWED_FAILURE_COUNT=2;
    ProgressBar loginProgressBar;
    TextView companyNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        Log.e("serial: ", Build.SERIAL);
        loginClickListener();
    }
    //
    private void initialize(){
        Context appContext=this;
        sharedPreferenceManager=new SharedPreferenceManager(appContext);
        toastAndAlertBuilder=new ToastAndAlertBuilder(appContext);
        loginButton=(Button)findViewById(R.id.loginButton);
        usercodeEditCode = (EditText) findViewById(R.id.editTextUserCode);
        passwordEditCode = (EditText) findViewById(R.id.editTextPassword);
        applicationVersion=(TextView) findViewById(R.id.applicationVersion);
        myVersionIs=(TextView)findViewById(R.id.myVersionIs);
        companyNameTextView=(TextView)findViewById(R.id.companyName);
        //
        face = Typeface.createFromAsset(appContext.getAssets(), "fonts/BZar.ttf");
        loginButton.setTypeface(face);
        applicationVersion.setTypeface(face);
        myVersionIs.setTypeface(face);
        //
        appVersion=BuildConfig.VERSION_NAME;
        applicationVersion.setText(appVersion);
        //
        loginProgressBar =(ProgressBar)findViewById(R.id.loginProgressBar);
        //
        setCompanyName();
    }
    //
    private void switchVisibility(){
        if(loginProgressBar.getVisibility()==View.GONE){
            loginProgressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }
        else {
            loginProgressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
    //
    private void loginClickListener(){
        //doLogin();//temp
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchVisibility();
                userCode = usercodeEditCode.getText().toString();
                password = passwordEditCode.getText().toString();
                getToken(userCode, password);
            }
        });
    }
    //
    private void getToken(final String username, final String password){
        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        final TokenRequestModel tokenRequestModel=new TokenRequestModel(username,password);
        Call<TokenResponseModel> call=abfaService.getToken(tokenRequestModel.username
                ,tokenRequestModel.password,
                tokenRequestModel.grant_type);
        call.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call,
                                   retrofit2.Response<TokenResponseModel> response) {
                TokenResponseModel tokenResponseModel = response.body();
                int responseCode = response.code();
                //
                //region_______________________ response implicit error___________________
                if (responseCode != 200) {
                    String errorMessage = "کد کاربری یا گذر واژه معتبر نمیباشد یا چنین کاربری ثبت نشده است.لطفا دوباره امتحان فرمایید";
                    toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                    failureCount = 0;
                    switchVisibility();
                    return;
                }
                //endregion

                sharedPreferenceManager.putString("token", "Bearer " + tokenResponseModel.access_token);
                sharedPreferenceManager.putInt("userCode", Integer.parseInt((username)));
                sharedPreferenceManager.putString("password", Crypto.encrypt(password));
                sharedPreferenceManager.apply();
                doLogin();
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                String errorMessage = "";
                toastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
                Log.e("retrofit token error", t.toString());
                ++failureCount;
                if (canILoginOffline()) {
                    if (checkLastCredential(username, password)) {
                        doLogin();
                    } else {
                        errorMessage = "نام کاربری و کامه عبور منطبق نیستند یا چنین کاربری وجود ندارد";
                        toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                        Log.e("invalid login attempt", " xxx");
                        switchVisibility();
                    }
                } else {
                    switchVisibility();
                }
            }
        });
    }
    //
    private boolean canILoginOffline(){
        if(!checkMobileDataIsOn()){
            return false;
        }
        if(failureCount<=ALLOWED_FAILURE_COUNT){
            return false;
        }
        if(sharedPreferenceManager.getString("token").length()<20){
            return false;
        }
        if(sharedPreferenceManager.getInt("userCode")==0){
            return false;
        }
        return true;
    }
    //
    private void doLogin(){
        Intent startupActivity = new Intent(LoginActivity.this, StartupActivity.class);
        startActivity(startupActivity);
        finish();
    }
    //
    private boolean checkMobileDataIsOn(){
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
            Log.e("data is ",mobileDataEnabled+"");
            return  mobileDataEnabled;
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
            Log.e("data is "," false catched error");
            return false;
        }
    }
    //
    private boolean checkLastCredential(String enterdUserCode,String enterdPassword){
        String prefUserCode=sharedPreferenceManager.getInt("userCode")+"";
        String prefPassword=Crypto.decrypt(sharedPreferenceManager.getString("password"));
        Log.e("prefUser",prefUserCode);
        Log.e("prefPass",password);
        if(prefUserCode.equals(enterdUserCode) && prefPassword.equals(enterdPassword)){
            return true;
        }
        Log.e("invalid"," credential in offline mode");
        return false;
    }
    //
    private void setCompanyName(){
        String companyName=DifferentCompanyManager.getCompanyName
                (DifferentCompanyManager.getActiveCompanyName());
        companyNameTextView.setText(companyName);
    }
}
