package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

/**
 * Created by saeid on 5/31/2017.
 */

public class ThemeFragment extends Fragment {

    RadioGroup radioThemeGroup;
    ISharedPreferenceManager sharedPreferenceManager;
    IToastAndAlertBuilder toastAndAlertBuilder;
    public ThemeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_theme, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView){
        radioThemeGroup=(RadioGroup)rootView.findViewById(R.id.radioThemeGroup);
        sharedPreferenceManager=new SharedPreferenceManager(getContext());
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        checkProperRadio();
        themeRadioListener();
    }
    private void themeRadioListener(){
        radioThemeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                View radioButton = radioGroup.findViewById(i);
                int indexOfChild = radioGroup.indexOfChild(radioButton);
                sharedPreferenceManager.put("theme",indexOfChild);
                sharedPreferenceManager.apply();
                toastAndAlertBuilder.makeSimpleAlert("همکار گرامی تغییر تم انجام شد، جهت مشاهده تغییر کافی " +
                        "است از نوار سمت راست یکی از بخش های برنامه را انتخاب فرمایید.");
            }
        });
    }

    private void checkProperRadio(){
        int themeChildIndex= sharedPreferenceManager.getInt("theme");
        ((RadioButton)radioThemeGroup.getChildAt(themeChildIndex)).setChecked(true);
    }
}
