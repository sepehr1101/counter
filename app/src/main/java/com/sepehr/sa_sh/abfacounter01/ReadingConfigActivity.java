package com.sepehr.sa_sh.abfacounter01;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.Fragments.DeleteDataFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.RouteConfigFragment;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class ReadingConfigActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_reading_config);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        viewPager = (ViewPager) findViewById(R.id.reading_config_viewpager_tab);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.reading_config_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new RouteConfigFragment(),"تنظیمات قرائت");
        adapter.addFragment(new BluetoothFragment(),"تنظیمات اتصال");
        adapter.addFragment(new DeleteDataFragment(),"حذف داده ها");
        viewPager.setAdapter(adapter);
    }
}
