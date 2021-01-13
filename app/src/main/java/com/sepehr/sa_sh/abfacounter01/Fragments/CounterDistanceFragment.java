package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.InputFilterMinMax;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

public class CounterDistanceFragment extends DialogFragment {
    String dbId;
    EditText d1,d2,l1,l2,r1,r2;
    Button cancel,ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  throws RuntimeException {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        View rootView = inflater.inflate(R.layout.fragment_counter_distance, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView){
        dbId=((DisplayViewPager)getActivity()).getId();

        d1=(EditText)rootView.findViewById(R.id.d1);
        d2=(EditText)rootView.findViewById(R.id.d2);
        l1=(EditText)rootView.findViewById(R.id.l1);
        l2=(EditText)rootView.findViewById(R.id.l2);
        r1=(EditText)rootView.findViewById(R.id.r1);
        r2=(EditText)rootView.findViewById(R.id.r2);
        cancel=(Button)rootView.findViewById(R.id.cancel);
        ok=(Button)rootView.findViewById(R.id.ok);

        d1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "30")});
        d2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        l1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "30")});
        l2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        r1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        r2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});

        cancelClickListenr();
        okClickListener();
    }
    private void updateRecordInDb(){
        int _d1,_d2,_r1,_r2,_l1,_l2;
        _d1=Integer.parseInt(d1.getText().toString());
        _d2=Integer.parseInt(d2.getText().toString());
        _l1=Integer.parseInt(l1.getText().toString());
        _l2=Integer.parseInt(l2.getText().toString());
        _r1=Integer.parseInt(r1.getText().toString());
        _r2=Integer.parseInt(r2.getText().toString());

        updateCounterDistance(dbId,_d1,_d2,_r1,_r2,_l1,_l2);
    }
    public void updateCounterDistance(String s_id,int d1,int d2,int r1,int r2,int l1,int l2){
        OnOffLoadModel onOffLoadModel=OnOffLoadModel.find(OnOffLoadModel.class,"SID=?",s_id).get(0);
        onOffLoadModel.d1=d1;
        onOffLoadModel.d2=d2;
        onOffLoadModel.r1=r1;
        onOffLoadModel.l1=l1;
        onOffLoadModel.l2=l2;
        onOffLoadModel.save();
    }

    private void clearEditTexts(){
        d1.setText("");
        d2.setText("");
        l1.setText("");
        l2.setText("");
        r1.setText("");
        r2.setText("");
    }

    private void cancelClickListenr(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEditTexts();
            }
        });
    }
    private void okClickListener(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecordInDb();
                dismiss();
            }
        });
    }
}
