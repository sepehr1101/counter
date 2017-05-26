package com.sepehr.sa_sh.abfacounter01.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.Fragments.LoadFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.ReloadFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.SpecialLoadFragment;
import com.sepehr.sa_sh.abfacounter01.Fragments.UrgentLoadFragment;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;

public class LoadActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_load);
        return uiElementInActivity;
    }

    @Override
    protected void initialize(){
        viewPager = (ViewPager) findViewById(R.id.load_viewpager_tab);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.load_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new LoadFragment(), "بارگیری");
        adapter.addFragment(new UrgentLoadFragment(),"بارگیری offline");
        adapter.addFragment(new SpecialLoadFragment(),"بارگیری ویژه");
        adapter.addFragment(new ReloadFragment(),"بارگیری مجدد");
        viewPager.setAdapter(adapter);
    }
}
