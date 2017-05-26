package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sepehr.sa_sh.abfacounter01.BaseClasses.LoadBase;
import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by _1101 on 10/20/2016.
 */
public class UrgentLoadFragment extends Fragment{
    public UrgentLoadFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_urgent_load, container, false);
        View rootView = inflater.inflate(R.layout.base_load_fragment, container, false);
        int imageId=R.drawable._urget_load;
        new LoadBase(getContext(),rootView,"بارگیری",imageId,true,false);
        return rootView;
    }

}
