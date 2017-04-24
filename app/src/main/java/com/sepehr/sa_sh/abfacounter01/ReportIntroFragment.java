package com.sepehr.sa_sh.abfacounter01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by saeid on 10/2/2016.
 */
public class ReportIntroFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.fragment_report_intro, container, false);
        Button report_intro_fragment=(Button)rootView.findViewById(R.id.report_intro_btn);
        report_intro_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoad=new Intent(getContext(),MyWorksReportActivity.class);
                startActivity(intentLoad);
            }
        });
        return rootView;
    }
}
