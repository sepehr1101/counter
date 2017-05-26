package com.sepehr.sa_sh.abfacounter01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sepehr.sa_sh.abfacounter01.Activities.LoadActivity;

/**
 * Created by saeid on 10/2/2016.
 */
public class LoadIntroFragment extends Fragment {
    public LoadIntroFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.fragment_load_intro, container, false);
        Button load_intro_btn=(Button)rootView.findViewById(R.id.load_intro_btn);
        load_intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoad = new Intent(getContext(), LoadActivity.class);
                startActivity(intentLoad);
            }
        });
        //final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");
       // loadText.setTypeface(face);
        return rootView;
    }
}
