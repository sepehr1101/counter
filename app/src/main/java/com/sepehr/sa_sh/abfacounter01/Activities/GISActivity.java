package com.sepehr.sa_sh.abfacounter01.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.Fragments.DisplayDeviceLocationFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.GisLocalLayersFragment;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class GISActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_gis);
        return uiElementInActivity;
    }
    @Override
    protected void initialize(){
        viewPager=(ViewPager)findViewById(R.id.gis_viewpager);
        tabLayout=(TabLayout)findViewById(R.id.gis_tabs);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(){
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new GisLocalLayersFragment(), "لایه ها");
        adapter.addFragment(new DisplayDeviceLocationFragment(),"مکان فعلی");
        viewPager.setAdapter(adapter);
    }

}
