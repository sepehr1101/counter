package com.sepehr.sa_sh.abfacounter01;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.sepehr.sa_sh.abfacounter01.Adopters.ViewPagerAdapterTab;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.BaseActivity;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IStatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.StatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.models.UiElementInActivity;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

public class MyWorksReportActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    IStatisticsRepo statisticsRepo;
    long tedadKolSize,unreadsSize,alalHesabsSize,masrafSefrSize,readSize;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity=new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.activity_my_works_report);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        viewPager = (ViewPager) findViewById(R.id.viewpagerTab);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        statisticsRepo=new StatisticsRepo(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new StatisticsFragment(),"آمار کلی");
        adapter.addFragment(new UnReadMoshtaraksFragment(), "قرائت نشده");
        adapter.addFragment(new AlalHesabFragment(), "علی الحساب");
        viewPager.setAdapter(adapter);
    }

    public long getTedadKolSize(){
        tedadKolSize=statisticsRepo.getKolSize();
        return tedadKolSize;
    }
    public  long getAlalHesabsSize(){
        alalHesabsSize= statisticsRepo.getAlalHesabSize();
        return alalHesabsSize;
    }
    public long getUnreadsSize(){
        unreadsSize=statisticsRepo.getUnreadSize();
        return  unreadsSize;
    }
    public  long getMasrafSefrSize(){
        masrafSefrSize= OnOffLoadModel.count(OnOffLoadModel.class,
                "COUNTER_NUMBER=PRE_NUMBER", null);
        return masrafSefrSize;
    }
    public long getReadSize(){
        readSize= tedadKolSize-(unreadsSize+masrafSefrSize+alalHesabsSize);
        return readSize;
    }
}
