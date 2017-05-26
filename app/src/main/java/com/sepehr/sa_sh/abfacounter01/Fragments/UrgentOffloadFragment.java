package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.OffloadBase;
import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by _1101 on 10/20/2016.
 */
public class UrgentOffloadFragment extends Fragment {

    public UrgentOffloadFragment() {
    }
    //Button urgentOffLoadBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_offload_fragment, container, false);
        int imageResoure=R.drawable._urget_offload;
        new OffloadBase(getContext(),rootView,"تخلیه Offline",imageResoure,true);
        return rootView;
    }

}
