package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterStateService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICounterStateService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IKarbariService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.KarbariService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import java.util.List;

/**
 * Created by sa-sh on 7/23/2016.
 */
public class ViewPagerAdaptor extends PagerAdapter {

    // Declare Variables
    IKarbariService karbariManager;
    IReadingConfigService readingConfigService;
    ICounterStateService counterStateManager;
    Typeface face;
    Context context;
    public List<OnOffLoadModel> _onOffList;
    LayoutInflater inflater;
    ReadingConfigModel readingConfig;
    String[] spinnerItems;
    List<CounterReportValueKeyModel> selectedReports;
    FragmentManager fragmentManager;

    public ViewPagerAdaptor(Context context,List<OnOffLoadModel> onOffLoadModelList,
                            String[] spinnerItems,FragmentManager fragmentManager,
                            List<CounterReportValueKeyModel> selectedReports) {
        face = Typeface.createFromAsset(context.getAssets(), "fonts/BZar.ttf");
        this.context = context;
        this._onOffList=onOffLoadModelList;
        this.readingConfigService=new ReadingConfigService();
        this.counterStateManager=new CounterStateService(true);
        this.spinnerItems=spinnerItems;
        this.selectedReports=selectedReports;
        this.fragmentManager=fragmentManager;
        this.karbariManager=new KarbariService();
    }

    @Override
    public int getCount() {
        return _onOffList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        OnOffLoadModel model=_onOffList.get(position);
        this.readingConfig=readingConfigService.get(model.trackNumber);
        String[] qotrList={"","1/2","3/4","1","2","3","4","5","6","7","8","9","10","11"};
        String[] qotrSifoonList={"","100","125","200"};
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,false);
        final MainViewPagerHolder viewPagerHolder=new MainViewPagerHolder(itemView);
        itemView.setTag(viewPagerHolder);

        // Capture position and set to the TextViews
        try {
            String karbariTitle = karbariManager.getKarbariTitle(model.getKarbariCod());
            viewPagerHolder.SetTextAndConfig(model,readingConfig,counterStateManager,spinnerItems,qotrList,qotrSifoonList,karbariTitle);
            //
        }
        catch (Exception e){
            Log.e("view adaptor error",e.getMessage());
        }
        setTag(viewPagerHolder,position);
        setTypeFace(viewPagerHolder);

        // Add viewpager_item.xml to ViewPager
                (container).addView(itemView);

        //region ____________________ spinner __________________________
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.spinner_custom_item, spinnerItems);
        adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        viewPagerHolder.counterStateSpinner.setAdapter(adapter);
        viewPagerHolder.counterStateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int i, long l) {
                viewPagerHolder.counterNumber.setText("");
                String selectedItemText= spinner.getSelectedItem().toString();
                ICounterStateService counterStateManager=new CounterStateService(selectedItemText,selectedReports);
                 if(counterStateManager.shouldIOpenBodyBox()){
                     CounterSerialFragment dialogFragment=new CounterSerialFragment();
                     dialogFragment.show(fragmentManager, "serial");
                 }
                 if(!counterStateManager.canEnterNumber()){
                    // viewPagerHolder.counterNumber.setText(spinner.getSelectedItem().toString());
                 }
            }
        });
        //endregion

        return itemView;
    }

    private void setTag(MainViewPagerHolder viewPagerHolder,int position){
        viewPagerHolder.billId.setTag("billId"+position);
        viewPagerHolder.eshterak.setTag("eshterak" + position);
        viewPagerHolder.address.setTag("address" + position);
        viewPagerHolder.counterNumber.setTag("counterNumber" + position);
        viewPagerHolder.counterStateSpinner.setTag("counterState" + position);
        viewPagerHolder.preNumber.setTag("preNumber"+position);
        viewPagerHolder.trackNumber.setTag("trackNumber" + position);
    }

    private void setTypeFace(MainViewPagerHolder viewPagerHolder){
        viewPagerHolder.shomareBadane.setTypeface(face);
        viewPagerHolder.preNumber.setTypeface(face);
        viewPagerHolder.billId.setTypeface(face);
        viewPagerHolder.firstLastName.setTypeface(face);
        viewPagerHolder.eshterak.setTypeface(face);
        viewPagerHolder.addressExpandable.setTypeface(face);
        //counterNumber.setTypeface(face);
        viewPagerHolder.tedadMaskooni.setTypeface(face);
        viewPagerHolder.tedadTejari.setTypeface(face);
        viewPagerHolder.karbari.setTypeface(face);
        viewPagerHolder.tedadKol.setTypeface(face);
        viewPagerHolder.preDate.setTypeface(face);
        viewPagerHolder.qotr.setTypeface(face);
        viewPagerHolder.qotrSifoon.setTypeface(face);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((RelativeLayout) object);
        View view = (View)object;
        view=null;
    }
}