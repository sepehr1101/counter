package com.sepehr.sa_sh.abfacounter01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class SettingActivity extends BaseActivity {
    ViewPager settingViewPager;
    TabLayout settingTabLayout;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_setting);
        return uiElementInActivity;
    }

    @Override
    protected void initialize(){
        settingViewPager=(ViewPager)findViewById(R.id.setting_viewpager);
        settingTabLayout=(TabLayout)findViewById(R.id.setting_tabs);
        setupViewPager(settingViewPager);
        settingTabLayout.setupWithViewPager(settingViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new SettingFragment(),"تغییر پسوورد");
        adapter.addFragment(new UpdateAppFragment(),"به روز رسانی");
        adapter.addFragment(new ReadingConfigFragment(),"تنظیمات قرائت");
        viewPager.setAdapter(adapter);
    }
}
