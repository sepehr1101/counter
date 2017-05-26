package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.OffloadBase;
import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by saeid on 5/25/2017.
 */

public class OffloadFragment extends Fragment {

    public OffloadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_offload_fragment, container, false);
        int imageResoure=R.drawable._offload_activity;
        new OffloadBase(getContext(),rootView,"تخلیه",imageResoure,false);
        return rootView;
    }
}
