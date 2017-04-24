package com.sepehr.sa_sh.abfacounter01;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.app.Dialog;

/**
 * Created by sa-sh on 8/10/2016.
 */
public class RegisterAnywayFragment extends DialogFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        View rootView = inflater.inflate(R.layout.fragment_register_anyway, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        AverageState averageState=((DisplayViewPager)getActivity()).getAverageStateGlobal();
        TextView alertTitle=(TextView)rootView.findViewById(R.id.alertTitleX);
        if(averageState==AverageState.MASRAF_SEFR){
            alertTitle.setText("مصرف صفر ، آیا صحت شماره کنتور را تایید مینمایید؟");
        }
        else if(averageState==AverageState.HIGH){
            alertTitle.setText("مصرف بالا ، آیا صحت شماره کنتور را تایید مینمایید؟");
        }
        //
        onNoButtonClick(rootView);
        onYesButtonClick(rootView);
        //
        return rootView;
    }
    //
    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
    //
    private void onNoButtonClick(View rootView){
        Button dismiss = (Button) rootView.findViewById(R.id.noButton);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //
    private void onYesButtonClick(View rootView){
        final Button dismiss = (Button) rootView.findViewById(R.id.yesButton);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DisplayViewPager) getActivity()).registerAnyway();
                dismiss();
            }
        });
    }
    //
}
