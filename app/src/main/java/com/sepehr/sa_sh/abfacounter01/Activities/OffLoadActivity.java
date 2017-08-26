package com.sepehr.sa_sh.abfacounter01.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.Fragments.OffloadFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.UrgentOffloadFragment;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class OffLoadActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_off_load);
        return uiElementInActivity;
    }
    @Override
    protected void initialize(){
        viewPager = (ViewPager) findViewById(R.id.offload_viewpager_tab);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.offload_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    //

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new OffloadFragment(), "تخلیه");
        adapter.addFragment(new UrgentOffloadFragment(),"تخلیه offline");
        viewPager.setAdapter(adapter);
    }

}
