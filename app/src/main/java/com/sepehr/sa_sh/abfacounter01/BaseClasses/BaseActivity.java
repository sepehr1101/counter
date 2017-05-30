package com.sepehr.sa_sh.abfacounter01.BaseClasses;

/**
 * Created by saeid on 3/10/2017.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sepehr.sa_sh.abfacounter01.Activities.LoadActivity;
import com.sepehr.sa_sh.abfacounter01.Activities.OffLoadActivity;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.MyWorksReportActivity;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.ReadingConfigActivity;
import com.sepehr.sa_sh.abfacounter01.AppSettingActivity;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

import java.lang.reflect.Method;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    //
    int userCode;
    String token,deviceId;
    //
    public IToastAndAlertBuilder toastAndAlertBuilder;
    public ISharedPreferenceManager sharedPreferenceManager;
    private UiElementInActivity uiElementInActivity;

    protected abstract UiElementInActivity getUiElementsInActivity();
    protected abstract void initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        super.onCreate(savedInstanceState);
        uiElementInActivity=getUiElementsInActivity();
        getTheme().applyStyle(R.style.OverlayPrimaryColorRed, true);
        setContentView(uiElementInActivity.getContentViewId());
        initializeBase();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        initialize();
    }
    //
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_load) {
            Intent intentLoad=new Intent(this,LoadActivity.class);
            startActivity(intentLoad);
        }
        else if (id == R.id.nav_my_works) {
            Intent intentDisplayViewpager=new Intent(this,DisplayViewPager.class);
            startActivity(intentDisplayViewpager);
        }
        else if (id == R.id.nav_off_load) {
            Intent offLoad = new Intent(this,OffLoadActivity.class);
            startActivity(offLoad);
        }
        else if (id == R.id.nav_reports) {
            Intent intentMyWorksReport = new Intent(this,MyWorksReportActivity.class);
            startActivity(intentMyWorksReport);
        }
        else if (id == R.id.nav_reading_config) {
            Intent intentUrgent = new Intent(this,ReadingConfigActivity.class);
            startActivity(intentUrgent);
        }
        else if (id == R.id.nav_location) {

        }
        else if (id == R.id.preferred_setting) {
            Intent intentSetting=new Intent(this,AppSettingActivity.class);
            startActivity(intentSetting);
        }
        else if(id==R.id.close_app){
            this.finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //
    private void initializeBase(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        sharedPreferenceManager=new SharedPreferenceManager(this);
    }
    //
    public int getUserCode(){
        userCode = sharedPreferenceManager.getInt("userCode");
        return userCode;
    }
    //
    public String getToken(){
        token = sharedPreferenceManager.getString("token");
        return token;
    }
    //
    /**
     * <h1>check if GPS is enabled</h1>
     *<p>check whether GPS is Enabled or not , if is disabled , enable it or exit</p>
     *@since 2016/08/26
     * @author _1101
     */
    public void turnOnGpsOrCloseApp(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("GPS IS:", enabled + "");
        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("تنظیمات جی پی اس");
            // Setting Dialog Message
            alertDialog.setMessage("مکان یابی شما غیر فعال است ،آیا مایلید به قسمت تنظیمات مکان یابی منتقل شوید");
            alertDialog.setPositiveButton("تنظیمات", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }

    public void turnOnDataOrCloseApp(){
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
            if (!mobileDataEnabled) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("تنظیمات دیتا");
                // Setting Dialog Message
                alertDialog.setMessage("داده های دستگاه شما خاموش است ، لطفا نسبت به روشن کردن آن اقدام فرمایید");
                alertDialog.setPositiveButton("تنظیمات", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
            Log.e("data is "," false catched error");
        }
    }
}

