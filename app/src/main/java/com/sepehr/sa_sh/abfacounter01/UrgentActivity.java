package com.sepehr.sa_sh.abfacounter01;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class UrgentActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_urgent);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        viewPager = (ViewPager) findViewById(R.id.urgent_viewpager_tab);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.urgent_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new UrgentLoadFragment(), "بارگیری offline");
        adapter.addFragment(new UrgentOffloadFragment(), "تخلیه offline");
        adapter.addFragment(new SpecialLoadFragment(),"بارگیری ویژه");
        adapter.addFragment(new BluetoothFragment(),"تنظیمات اتصال");
        viewPager.setAdapter(adapter);
    }
}
