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
public class OffLoadIntorFragment extends Fragment{
    public OffLoadIntorFragment() {
    }
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.fragment_offload_intro, container, false);
        Button offload_intro_btn=(Button)rootView.findViewById(R.id.offload_intro_btn);
        offload_intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent offLoad = new Intent(getContext(),OffLoadActivity.class);
                startActivity(offLoad);
            }
        });
        return rootView;
    }
}
