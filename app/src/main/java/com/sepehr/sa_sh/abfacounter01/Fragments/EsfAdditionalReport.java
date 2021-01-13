package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IKarbariService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.KarbariService;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.OffloadState;
import com.sepehr.sa_sh.abfacounter01.models.ReportCheckboxModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EsfAdditionalReport extends DialogFragment
        implements AdapterView.OnItemSelectedListener{

    TextView mas,tej,sai,preKarbari,preMobile;
    EditText tMas,tTej,tSai,possibleMobile;
    Button register,cancel;
    Spinner spinnerKarbari;
    String bill_id;
    BigDecimal trackNumber;
    View rootView;
    IToastAndAlertBuilder toastAndAlertBuilder;
    RadioGroup radioGroupQatVasl,radioGroupMahalCounter,radioGroupNasht,radioGroupSaxt;
    RadioButton qat,vasl,counterMonaseb,counterNaMonaseb,nashtDarad,nashtNadarad,saxtAst,saxtNist;
    CounterReportService manager;
    IKarbariService karbariService;
    List<CounterReportValueKeyModel> selectedReports;
    OnOffLoadModel currentData;
    String karbari;
    int karbariId=-1,radioSelectedCount=0;
    Dialog dialog;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rootView = inflater.inflate(R.layout.fragment_esf_additional_report, container, false);
        initialize(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                toastAndAlertBuilder.makeSimpleToast("همکار گرامی برای بستن فرم لطفا اطلاعات پیمایشی را تکمیل فرمایید");
            }
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

        mas=(TextView)rootView.findViewById(R.id.tedadMaskooniRd) ;
        tej=(TextView)rootView.findViewById(R.id.tedadTejariRd) ;
        sai=(TextView)rootView.findViewById(R.id.tedadSaierRd) ;
        preKarbari=(TextView)rootView.findViewById(R.id.preKarbari);
        preMobile=(TextView)rootView.findViewById(R.id.preMobile);

        tMas=(EditText) rootView.findViewById(R.id.tMas) ;
        tTej=(EditText)rootView.findViewById(R.id.tTej) ;
        tSai=(EditText)rootView.findViewById(R.id.tSai) ;

        possibleMobile=(EditText)rootView.findViewById(R.id.possibleMobile);

        radioGroupQatVasl=(RadioGroup) rootView.findViewById(R.id.radioQatVasl);
        radioGroupMahalCounter=(RadioGroup) rootView.findViewById(R.id.radioVaziatCounter);
        radioGroupNasht=(RadioGroup) rootView.findViewById(R.id.radioNasht);
        radioGroupSaxt=(RadioGroup) rootView.findViewById(R.id.radioSaxt);

        qat=(RadioButton)rootView.findViewById(R.id.radioqat);
        vasl=(RadioButton)rootView.findViewById(R.id.radioVasl);
        counterMonaseb=(RadioButton)rootView.findViewById(R.id.radioCounterMonaseb);
        counterNaMonaseb=(RadioButton)rootView.findViewById(R.id.radioCounterNaMonaseb);
        nashtDarad=(RadioButton)rootView.findViewById(R.id.radioNashtDarad);
        nashtNadarad=(RadioButton)rootView.findViewById(R.id.radioNashtNadarad);
        saxtAst=(RadioButton)rootView.findViewById(R.id.radioSaxtAst);
        saxtNist=(RadioButton)rootView.findViewById(R.id.radioSaxtNist);

        register=(Button)rootView.findViewById(R.id.registerEsf);
        cancel=(Button)rootView.findViewById(R.id.cancelEsf);
        spinnerKarbari=(Spinner) rootView.findViewById(R.id.spinner);
        spinnerKarbari.setOnItemSelectedListener(this);

        manager=new CounterReportService(bill_id);
        karbariService = new KarbariService();
        setCancelClickListener();
        setRegisterClickListener();
        selectedReports = manager.getCounterReadingSelectedReports(bill_id);
        setRadios();
        setAhad();
        setKarbari();
        setPossibleMobile();
        setPreMobile();
        radioClickListeners();
        cancelClosing();

        List<String> categories = new ArrayList(){};
        categories.add("کاربری");
        categories.add("مسکونی");
        categories.add("تجاری");
        categories.add("مسکونی-تجاری");
        categories.add("آموزشی");
        categories.add("مذهبی");
        categories.add("بهداشتی درمانی");
        categories.add("سایر");

        ArrayAdapter dataAdapter = new ArrayAdapter(getContext(), R.layout.spinner_custom_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKarbari.setAdapter(dataAdapter);
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
                    String m= tMas.getText().toString().equals("") || tMas.getText()==null ?"": tMas.getText().toString();
                    String t=tTej.getText().toString().equals("")|| tTej.getText()==null  ?"": tTej.getText().toString();

                    currentData.possibleTedadMaskooni=m;
                    currentData.possibleTedadTejari=t;
                    currentData.offLoadState=OffloadState.ABOUT_TO_SEND;
                    id=currentData.s_id;
                    OnOffLoadModel onOffLoadModel=OnOffLoadModel
                            .find(OnOffLoadModel.class, "SID = ? ", id)
                            .get(0);
                    if(possibleMobile.getText()!=null && !possibleMobile.getText().toString().equals("")){
                        if(possibleMobile.getText().toString().length()<11){
                            toastAndAlertBuilder.makeSimpleToast("تلفن همراه 11 رقمی است");
                            return;
                        }
                        if(possibleMobile.getText().toString().length()>11){
                            toastAndAlertBuilder.makeSimpleToast("تلفن همراه 11 رقمی است");
                            return;
                        }
                        if(!possibleMobile.getText().toString().subSequence(0,2).equals("09")){
                            toastAndAlertBuilder.makeSimpleToast("تلفن همراه با 09 شروع میشود");
                            return;
                        }
                        currentData.possibleMobile=possibleMobile.getText().toString();
                        onOffLoadModel.possibleMobile=possibleMobile.getText().toString();
                    }
                    onOffLoadModel.possibleTedadMaskooni=m;
                    onOffLoadModel.possibleTedadTejari=t;
                    onOffLoadModel.offLoadState= OffloadState.ABOUT_TO_SEND;
                    if(karbariId!= -1){
                        currentData.possibleKarbariCode=karbariId+"";
                        onOffLoadModel.possibleKarbariCode=karbariId+"";
                    }

                    currentData.counterStatePosition=DisplayViewPager.globalCurrentCounterPositionForFragment;
                    currentData.counterStateCode=DisplayViewPager.globalCurrentCounterStateCode;
                    currentData.save();
                    onOffLoadModel.save();
                    DisplayViewPager motherActivity=(DisplayViewPager)getActivity();
                    motherActivity.goNextPage();
                    boolean canIdLeave=canLeaveDialoge();
                    Log.e("leave",canIdLeave+"");
                    Log.e("leave",radioSelectedCount+"");
                    if(canIdLeave) {
                        dismiss();
                    }else {
                        toastAndAlertBuilder.makeSimpleToast("همکار گرامی لطفا گزارش پیمایش را تکمیل فرمایید");
                    }
                }catch (Exception e){
                    toastAndAlertBuilder.makeSimpleToast("همکار گرامی خطا رخ داد"+"\n"+e.getMessage());
                }
            }
        });
    }
    private void radioClickListeners(){
        qat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==30){
                       manager.removeReport(30);
                        --radioSelectedCount;
                    }
                    if(code==31){
                        manager.removeReport(31);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(30);
                reportCheckboxModel.setTITLE("قطع");
                ++radioSelectedCount;
                manager.saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        vasl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==30){
                        manager.removeReport(30);
                        --radioSelectedCount;
                    }
                    if(code==31){
                        manager.removeReport(31);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(31);
                reportCheckboxModel.setTITLE("وصل");
                ++radioSelectedCount;
                manager.saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        //
        counterMonaseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==32){
                        manager.removeReport(32);
                        --radioSelectedCount;
                    }
                    if(code==33){
                        manager.removeReport(33);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(32);
                reportCheckboxModel.setTITLE("وضعیت محل کنتور مناسب");
                ++radioSelectedCount;
                manager .saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        counterNaMonaseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==32){
                        manager.removeReport(32);
                        --radioSelectedCount;
                    }
                    if(code==33){
                        manager.removeReport(33);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(33);
                reportCheckboxModel.setTITLE("وضعیت محل کنتور نامناسب");
                ++radioSelectedCount;
                manager .saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        //
        nashtDarad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==34){
                        manager.removeReport(34);
                        --radioSelectedCount;
                    }
                    if(code==35){
                        manager.removeReport(35);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(34);
                reportCheckboxModel.setTITLE("نشت آب حوزچه دارد");
                ++radioSelectedCount;
                manager .saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        nashtNadarad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==34){
                        manager.removeReport(34);
                        --radioSelectedCount;
                    }
                    if(code==35){
                        manager.removeReport(35);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(35);
                reportCheckboxModel.setTITLE("نشت آب حوزچه ندارد");
                ++radioSelectedCount;
                manager .saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        //
        saxtAst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==36){
                        manager.removeReport(36);
                        --radioSelectedCount;
                    }
                    if(code==37){
                        manager.removeReport(37);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(36);
                reportCheckboxModel.setTITLE("در حال ساخت میباشد");
                ++radioSelectedCount;
                manager .saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        saxtNist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReports = manager.getCounterReadingSelectedReports(bill_id);
                for (CounterReportValueKeyModel selectedReport:selectedReports) {
                    int code=selectedReport.getCODE();
                    if(code==36){
                        manager.removeReport(36);
                        --radioSelectedCount;
                    }
                    if(code==37){
                        manager.removeReport(37);
                        --radioSelectedCount;
                    }
                }
                ReportCheckboxModel reportCheckboxModel=new ReportCheckboxModel();
                reportCheckboxModel.setIsChecked(true);
                reportCheckboxModel.setReportCode(37);
                reportCheckboxModel.setTITLE("در حال ساخت نیست");
                ++radioSelectedCount;
                manager .saveReport(reportCheckboxModel, 0,trackNumber);
            }
        });
        //
    }

    private void setRadios(){
        for (CounterReportValueKeyModel selectedReport:selectedReports) {
            int code=selectedReport.getCODE();
            if(code==30){
                qat.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==31){
                vasl.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==32){
                counterMonaseb.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==33){
                counterNaMonaseb.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==34){
                nashtDarad.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==35){
                nashtNadarad.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==36){
                saxtAst.setChecked(true);
                ++radioSelectedCount;
            }
            if(code==37){
                saxtNist.setChecked(true);
                ++radioSelectedCount;
            }
        }
    }
    private void setAhad(){
        if(currentData!=null && currentData.tedadMaskooni!=null){
            mas.setText(currentData.tedadMaskooni+"");
        }
        if(currentData!=null && currentData.tedadNonMaskooni!=null){
            tej.setText(currentData.tedadNonMaskooni+"");
        }
        if(currentData!=null && currentData.tedadKol!=null){
            sai.setText(currentData.tedadKol+"");
        }
        //
        if(currentData!=null && currentData.possibleTedadMaskooni!=null){
            tMas.setText(currentData.possibleTedadMaskooni+"");
        }
        if(currentData!=null && currentData.possibleTedadTejari!=null){
            tTej.setText(currentData.possibleTedadTejari+"");
        }
    }
    private void setKarbari(){
        KarbariModel karbari=karbariService.get(currentData.getKarbariCod());
        if(karbari!=null && karbari.getTITLE()!=null){
            preKarbari.setText(karbari.getTITLE().toString());
        }
    }
    private void setPreMobile(){
        String mobile=currentData.getMobile();
        if(mobile!=null && !mobile.isEmpty()){
            preMobile.setText(mobile);
        }
    }
    private void setPossibleMobile(){
        if(currentData.possibleMobile!=null){
            possibleMobile.setText(currentData.possibleMobile.toString());
        }
    }

    private boolean canLeaveDialoge(){
       /* if(currentData.counterStateCode==null && currentData.counterStatePosition==null) {//از روی مثلث بدون ثبت قبلی
            return true;
        }
        if(radioSelectedCount>=2  &&(currentData.counterStatePosition==3 || currentData.counterStateCode==9)){//بسته
            return true;
        }
        if(radioSelectedCount<4 && (currentData.counterStateCode!=4 || currentData.counterStateCode!=7) ){//عادی
            return false;
        }*/
        if(currentData.counterStateCode==null && currentData.counterStatePosition==null) {//از روی مثلث بدون ثبت قبلی
            return true;
        }
        if(radioSelectedCount>=1  &&(currentData.counterStatePosition==3 || currentData.counterStateCode==9)){//بسته
            return true;
        }
        if(radioSelectedCount<1 && (currentData.counterStateCode!=4 || currentData.counterStateCode!=7) ){//عادی
            return false;
        }
        if( (tMas.getText().toString().equals("") || tMas.getText()==null) && currentData.counterStateCode!=null){// مسکونی خالی
            if(currentData.counterStateCode!=4 && currentData.counterStateCode!=7){
               return false;
            }
        }
        if( (tTej.getText().toString().equals("") || tTej.getText()==null)  && currentData.counterStateCode!=null){//تجاری خالی
            if(currentData.counterStateCode!=4 && currentData.counterStateCode!=7){
                return false;
            }
        }
        if(karbariId==-1){
            return false;
        }
        return true;
    }
    private void cancelClosing(){
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        karbari=item;
        switch (position) {
            case 0:
                karbariId = -1;
                break;
            case 1:
                karbariId = 1;
                break;
            case 2:
                karbariId=2;
                break;
            case 3:
                karbariId=3;
                break;
            case 4:
                karbariId=33;
                break;
            case 5:
                karbariId=6;
                break;
            case 6:
                karbariId=12;
                break;
            case 7:
                karbariId=44;
                break;
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
