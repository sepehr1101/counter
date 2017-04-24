package com.sepehr.sa_sh.abfacounter01;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by saeid on 10/20/2016.
 */
public class UrgentLoadFragment extends Fragment{
    public UrgentLoadFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_urgent_load, container, false);
        final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");


        return rootView;
    }
}
