package com.sepehr.sa_sh.abfacounter01;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterStateService;
/*import com.manolovn.trianglify.TrianglifyDrawable;
import com.manolovn.trianglify.TrianglifyView;*/

/**
 * Created by sa-sh on 8/12/2016.
 */
public class AlalHesabFragment extends Fragment {
    TextView tedadKolText,tedadKolVal,alalHesabText,alalHesabVal;
    String[] spinnerItems;
    Spinner maneSpinner;
    public AlalHesabFragment() {
        // Required empty public constructor
    }

    private void initialize(View rootView){
        maneSpinner=(Spinner)rootView.findViewById(R.id.alalHesabSpinner);
        spinnerItems= CounterStateService.getManeTitles();
        setSpinner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_alalhesab, container, false);

        final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");

        tedadKolText=(TextView)rootView.findViewById(R.id.tedadKolText);
        tedadKolVal=(TextView) rootView.findViewById(R.id.tedadKolVal);
        alalHesabText=(TextView) rootView.findViewById(R.id.tedadAlalHesabText);
        alalHesabVal=(TextView) rootView.findViewById(R.id.tedadAlalHesabVal);

        tedadKolText.setTypeface(face);
        tedadKolVal.setTypeface(face);
        alalHesabVal.setTypeface(face);
        alalHesabText.setTypeface(face);

        long alalHesabsSize=((MyWorksReportActivity)getActivity()).getAlalHesabsSize();
        alalHesabVal.setText(alalHesabsSize+"");
        long tedadKolSize=((MyWorksReportActivity)getActivity()).getTedadKolSize();
        tedadKolVal.setText(tedadKolSize+"");
        showAlalHesabs(rootView);
        initialize(rootView);
        return rootView;
    }
    //
    private void showAlalHesabs(View rootView){
        final Button showAlalHesabHa=(Button)rootView.findViewById(R.id.showAlalHesabHa);
        showAlalHesabHa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDisplayViewpager=new Intent(getContext(),DisplayViewPager.class);
                intentDisplayViewpager.putExtra("s",1);//s==1 -> alal hesab
                startActivity(intentDisplayViewpager);
            }
        });
    }
    //
    private void setSpinner(){
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(getContext(),R.layout.spinner_custom_item, spinnerItems);
        adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        maneSpinner.setAdapter(adapter);
        maneSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int i, long l) {
                int maneCode= CounterStateService.getManeCode(spinner.getSelectedItemPosition());
                Intent intentDisplayViewpager=new Intent(getContext(),DisplayViewPager.class);
                intentDisplayViewpager.putExtra("s", maneCode);
                startActivity(intentDisplayViewpager);
            }
        });
    }
}
