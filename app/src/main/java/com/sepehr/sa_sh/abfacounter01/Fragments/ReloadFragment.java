package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sepehr.sa_sh.abfacounter01.BaseClasses.LoadBase;
import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by saeid on 5/26/2017.
 */

public class ReloadFragment extends Fragment {
    public ReloadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.base_load_fragment, container, false);
        int imageId=R.drawable._load_activity;
        new LoadBase(getContext(),rootView,"بارگیری مجدد",imageId,true,true);
        return rootView;
    }
}
