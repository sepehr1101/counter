package com.sepehr.sa_sh.abfacounter01;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

/**
 * Created by sa-sh on 8/14/2016.
 */
public class StatisticsFragment extends Fragment {
    TextView sKolText,sKolVal,sAlalHesabText,sAlalHesabVal,sUnreadText,sUnreadVal,
            sMasrafSefrText,sMasrafSefrVal,sReadText,sReadVal;
    public StatisticsFragment() {
    }

    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        PieChart mPieChart = (PieChart) rootView.findViewById(R.id.piechart);

        final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");

        long tedadKolSize=((MyWorksReportActivity)getActivity()).getTedadKolSize();
        long unreadsSize=((MyWorksReportActivity)getActivity()).getUnreadsSize();
        long alalHesabsSize=((MyWorksReportActivity)getActivity()).getAlalHesabsSize();
        long masrafSefrSize=((MyWorksReportActivity)getActivity()).getMasrafSefrSize();
        long readSize=tedadKolSize-(unreadsSize+masrafSefrSize+alalHesabsSize);

        sKolVal=(TextView)rootView.findViewById(R.id.sKolVal);
        sAlalHesabVal=(TextView)rootView.findViewById(R.id.sAlalHesabVal);
        sUnreadVal=(TextView)rootView.findViewById(R.id.sUnreadVal);
        sMasrafSefrVal=(TextView)rootView.findViewById(R.id.sMasrafSefrVal);
        sReadVal=(TextView)rootView.findViewById(R.id.sReadVal);

        sKolText=(TextView)rootView.findViewById(R.id.sKolText);
        sUnreadText=(TextView)rootView.findViewById(R.id.sUnreadText);
        sAlalHesabText=(TextView)rootView.findViewById(R.id.sAlalHesabText);
        sMasrafSefrText=(TextView)rootView.findViewById(R.id.sMasrafSefrText);
        sReadText=(TextView)rootView.findViewById(R.id.sReadText);

        sKolVal.setText(tedadKolSize+"");
        sReadVal.setText(readSize+"");
        sUnreadVal.setText(unreadsSize+"");
        sAlalHesabVal.setText(alalHesabsSize + "");
        sMasrafSefrVal.setText(masrafSefrSize + "");

        sKolVal.setTypeface(face);
        sReadVal.setTypeface(face);
        sUnreadVal.setTypeface(face);
        sAlalHesabVal.setTypeface(face);
        sMasrafSefrVal.setTypeface(face);

        sKolText.setTypeface(face);
        sReadText.setTypeface(face);
        sUnreadText.setTypeface(face);
        sAlalHesabText.setTypeface(face);
        sMasrafSefrText.setTypeface(face);

        mPieChart.addPieSlice(new PieModel("علی الحساب",alalHesabsSize, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("قرائت شده", readSize, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("قرائت نشده", unreadsSize, Color.parseColor("#FF4081")));//CDA67F
        mPieChart.addPieSlice(new PieModel("مصرف صفر", masrafSefrSize, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();

        return rootView;
    }

}
