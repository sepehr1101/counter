package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.DifferentCompanyManager;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.constants.CompanyNames;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.InputFilterMinMax;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.OffloadState;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;

public class MobileReport extends DialogFragment{

    EditText preMobile;
    EditText possibleMobileEditText;
    Button register,cancel;
    String bill_id;
    BigDecimal trackNumber;
    View rootView;
    IToastAndAlertBuilder toastAndAlertBuilder;
    CounterReportService manager;
    OnOffLoadModel currentData;
    Dialog dialog;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rootView = inflater.inflate(R.layout.fragment_mobile_report, container, false);
        initialize(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
           /* @Override
            public void onBackPressed() {
                toastAndAlertBuilder.makeSimpleToast("همکار گرامی برای بستن فرم لطفا اطلاعات پیمایشی را تکمیل فرمایید");
            }*/
        };
    }

    private void initialize(View rootView){
        dialog=getDialog();
        DisplayViewPager motherActivity=(DisplayViewPager)getActivity();
        bill_id=motherActivity.getBill_id();
        trackNumber=motherActivity.getCurrentTrackNumber();
        currentData=motherActivity._list.get(DisplayViewPager.currentPosition);
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        id=motherActivity.getId();

        preMobile=(EditText) rootView.findViewById(R.id.preMobile);
        possibleMobileEditText =(EditText)rootView.findViewById(R.id.newMobile);
        possibleMobileEditText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(11)});

        register=(Button)rootView.findViewById(R.id.registerEsf);
        cancel=(Button)rootView.findViewById(R.id.cancelEsf);
        manager=new CounterReportService(bill_id);
        setCancelClickListener();
        setRegisterClickListener();
        setMobiles();
        //cancelClosing();
        possibleMobileEditText.requestFocus();
    }
    private void setCancelClickListener(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void setRegisterClickListener(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(possibleMobileEditText.getText()==null || possibleMobileEditText.getText().toString().length()<11){
                        toastAndAlertBuilder.makeSimpleToast("تلفن همراه 11 رقمی است");
                        return;
                    }
                    if(possibleMobileEditText.getText().toString().length()>11){
                        toastAndAlertBuilder.makeSimpleToast("تلفن همراه 11 رقمی است");
                        return;
                    }
                    if(!possibleMobileEditText.getText().toString().subSequence(0,2).equals("09")){
                        toastAndAlertBuilder.makeSimpleToast("تلفن همراه با 09 شروع میشود");
                        return;
                    }
                    currentData.offLoadState=OffloadState.ABOUT_TO_SEND;
                    id=currentData.s_id;
                    OnOffLoadModel onOffLoadModel=OnOffLoadModel
                            .find(OnOffLoadModel.class, "SID = ? ", id)
                            .get(0);
                    if(possibleMobileEditText.getText()!=null && !possibleMobileEditText.getText().toString().equals("")){
                        currentData.possibleMobile= possibleMobileEditText.getText().toString();
                        onOffLoadModel.possibleMobile= possibleMobileEditText.getText().toString();
                    }
                    currentData.counterStatePosition=DisplayViewPager.globalCurrentCounterPositionForFragment;
                    currentData.counterStateCode=DisplayViewPager.globalCurrentCounterStateCode;
                    currentData.save();
                    onOffLoadModel.offLoadState=OffloadState.ABOUT_TO_SEND;
                    onOffLoadModel.save();
                    DisplayViewPager motherActivity=(DisplayViewPager)getActivity();
                    motherActivity.goNextPage();
                    boolean canIdLeave=canLeaveDialoge();
                    canIdLeave=true;
                    Log.e("leave",canIdLeave+"");
                    if(canIdLeave) {
                        dismiss();
                    }else {
                        dismiss();
                        //toastAndAlertBuilder.makeSimpleToast("همکار گرامی لطفا گزارش پیمایش را تکمیل فرمایید");
                    }
                }catch (Exception e){
                    toastAndAlertBuilder.makeSimpleToast("همکار گرامی خطا رخ داد"+"\n"+e.getMessage());
                }
            }
        });
    }
    private void setMobiles(){
        if(currentData.possibleMobile!=null){
            possibleMobileEditText.setText(currentData.possibleMobile.toString());
        }
        if(currentData.mobile!=null){
            preMobile.setText(currentData.mobile.toString());
        }
    }

    private boolean canLeaveDialoge(){
        if(DifferentCompanyManager.getActiveCompanyName()== CompanyNames.KERMANSHAH){
            return true;
        }
        if(currentData.counterStateCode==null && currentData.counterStatePosition==null) {//از روی مثلث بدون ثبت قبلی
            return true;
        }
        if(currentData.counterStatePosition==3 || currentData.counterStateCode==9){//بسته
            return true;
        }
        if(currentData.counterStateCode!=4 || currentData.counterStateCode!=7 ){//عادی
            return false;
        }
        if((possibleMobileEditText.getText().toString().equals("")) && currentData.counterStateCode!=null){//موبایل جدید خالی
            if(currentData.counterStateCode!=4 && currentData.counterStateCode!=7){
                return false;
            }
        }
        return true;
    }
    private void cancelClosing(){
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }
}
