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
public class FlashIntorFragment extends Fragment {
    public FlashIntorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.fragment_flash_intro, container, false);
        Button flash_intro_btn=(Button)rootView.findViewById(R.id.flash_intro_btn);
        flash_intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFlash = new Intent(getContext(), FlashLight.class);
                startActivity(intentFlash);
            }
        });
        return rootView;
    }
}
