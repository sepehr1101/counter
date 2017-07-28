package com.sepehr.sa_sh.abfacounter01;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.Fragments.ChangePasswordFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.RouteConfigFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.ThemeFragment;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class AppSettingActivity extends BaseActivity {
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
        adapter.addFragment(new ChangePasswordFragment(),"تغییر پسوورد");
        adapter.addFragment(new UpdateAppFragment(),"به روز رسانی");
        adapter.addFragment(new ThemeFragment(),"تغییر تم");
        viewPager.setAdapter(adapter);
    }
}
