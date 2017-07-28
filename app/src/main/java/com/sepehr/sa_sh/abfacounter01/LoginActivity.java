package com.sepehr.sa_sh.abfacounter01;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.Activities.StartupActivity;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
    final int ALLOWED_FAILURE_COUNT=3;
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLocal=false;
                switchVisibility();
                userCode = usercodeEditCode.getText().toString();
                password = passwordEditCode.getText().toString();
                if(userCode.length()>1){
                    String userFirstChar= userCode.substring(0,2);
                    if(userFirstChar.equals("00")){
                        userCode=userCode.substring(1,userCode.length());
                        isLocal=true;
                    }
                }
                getToken(userCode, password,isLocal);
            }
        });
    }
    //
    private void getToken(final String username, final String password,boolean islocal){
        IAbfaService abfaService = NetworkHelper.getInstance(islocal).create(IAbfaService.class);
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

                sharedPreferenceManager.put("token", "Bearer " + tokenResponseModel.access_token);
                sharedPreferenceManager.put("userCode", Integer.parseInt((username)));
                sharedPreferenceManager.put("password", Crypto.encrypt(password));
                sharedPreferenceManager.apply();
                doLogin();
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                String errorMessage ;
                if(!isFinishing()){
                toastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
                }
                Log.e("retrofit token error", t.toString());
                ++failureCount;
                if (canILoginOffline()) {
                    if (checkLastCredential(username, password)) {
                        doLogin();
                    } else {
                        errorMessage = "نام کاربری و کامه عبور منطبق نیستند یا چنین کاربری وجود ندارد";
                        if(!isFinishing()) {
                            toastAndAlertBuilder.makeSimpleAlert(errorMessage);
                        }
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
    public boolean canIPingBaseUrl(){
        try {
          Boolean result=  new Ping().execute("").get();
            return result;
        }catch (InterruptedException e){
            return false;
        }catch (ExecutionException e){
            return false;
        }
    }
    //
    private boolean canILoginLocally(){
        if(/*!isWifiOn() || */ !canIPingBaseUrl()){
            return false;
        }
        if(failureCount<ALLOWED_FAILURE_COUNT){
            return false;
        }
        return true;
    }
    //
    private boolean isWifiOn(){
        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifi.isWifiEnabled()){
            return true;
        }
        return false;
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
    //
    //
    class Ping extends AsyncTask<String, Boolean, Boolean> {
        private Exception exception;
        protected Boolean doInBackground(String... urls) {
            try {
                try {
                    URL url = new URL("http://192.168.42.12:45455");

                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("User-Agent", "Android Application:");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1000 * 3); // mTimeout is in seconds
                    urlc.connect();

                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return false;
            } catch (Exception e) {
                this.exception = e;
                return false;
            }
        }

        protected void onPostExecute(Boolean feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}
