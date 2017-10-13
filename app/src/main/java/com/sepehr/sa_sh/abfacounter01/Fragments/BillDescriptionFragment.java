package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IOnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by saeid on 9/9/2017.
 */

public class BillDescriptionFragment extends DialogFragment{
    View rootView;
    EditText description;
    Button exit,setDescription;
    IToastAndAlertBuilder toastAndAlertBuilder;
    String billId;
    OnOffLoadModel onOffLoadModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rootView = inflater.inflate(R.layout.fragment_bill_description, container, false);
        initialize(rootView);
        return rootView;
    }

    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void initialize(View rootView){
        billId=((DisplayViewPager)getActivity()).getBill_id();
        onOffLoadModel = OnOffLoadModel.find(OnOffLoadModel.class, "BILL_ID= ? ", billId).get(0);
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        setDescription=(Button)rootView.findViewById(R.id.ok);
        exit=(Button)rootView.findViewById(R.id.dismiss);
        description=(EditText)rootView.findViewById(R.id.description);
        description.setText(onOffLoadModel.getDescription());
        exitClick();
        setDescriptionClick();
    }

    private void exitClick(){
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setDescriptionClick(){
        setDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descriptiontext=(description.getText().length()>0 )?
                        description.getText().toString():"";
                onOffLoadModel.setDescription(descriptiontext);
                onOffLoadModel.save();
                Toast.makeText(getContext(), "همکار گرامی اطلاعات ذخیره شد  جهت ارسال ثبت را فشار دهید",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
