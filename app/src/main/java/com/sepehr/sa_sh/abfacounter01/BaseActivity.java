package com.sepehr.sa_sh.abfacounter01;

/**
 * Created by saeid on 3/10/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    //
    int userCode;
    String token,deviceId;
    //
    IToastAndAlertBuilder toastAndAlertBuilder;
    public ISharedPreferenceManager sharedPreferenceManager;
    private UiElementInActivity uiElementInActivity;

    protected abstract UiElementInActivity getUiElementsInActivity();
    protected abstract void initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        super.onCreate(savedInstanceState);
        uiElementInActivity=getUiElementsInActivity();
        setContentView(uiElementInActivity.getContentViewId());
        initializeBase();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
        else if (id == R.id.nav_urgent) {
            Intent intentUrgent = new Intent(this,UrgentActivity.class);
            startActivity(intentUrgent);
        }
        else if (id == R.id.nav_flash_light) {
            Intent intentFlashLight=new Intent(this,FlashLight.class);
            startActivity(intentFlashLight);
        }
        else if (id == R.id.nav_location) {

        }
        else if (id == R.id.preferred_setting) {
            Intent intentSetting=new Intent(this,SettingActivity.class);
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
}

