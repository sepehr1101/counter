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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.DateAndTime;
import com.sepehr.sa_sh.abfacounter01.DifferentCompanyManager;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.constants.CompanyNames;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.OffloadState;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OneTimeCodeFragment extends DialogFragment{

    EditText oneTimeCodeEditText;
    TextView oneTimeCodeTextView;
    Button register,cancel;
    IToastAndAlertBuilder toastAndAlertBuilder;
    Dialog dialog;
    ISharedPreferenceManager sharedPreferenceManager;
    View rootView;
    LinearLayout codeEntrance,codeDisplay;
    String sharedPreferanceKey = "oneTimeCode";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rootView = inflater.inflate(R.layout.fragment_one_time_code, container, false);
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
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        oneTimeCodeEditText=(EditText) rootView.findViewById(R.id.mamoorOneTimeCode);
        oneTimeCodeTextView=(TextView)rootView.findViewById(R.id.oneTimeCode);
        register=(Button)rootView.findViewById(R.id.registerOneTimeCode);
        cancel=(Button)rootView.findViewById(R.id.cancelOneTimeCode);
        codeEntrance=(LinearLayout)rootView.findViewById(R.id.oneTimeCodeEntrance);
        codeDisplay=(LinearLayout)rootView.findViewById(R.id.oneTimeCodeDisplay);
        sharedPreferenceManager=new SharedPreferenceManager(getContext());
        setCancelClickListener();
        setRegisterClickListener();

        try {
            if (isOneTimeCodeEntered()) {
                codeEntrance.setVisibility(View.GONE);
                codeDisplay.setVisibility(View.VISIBLE);
                int oneTimeCode=getOneTimeCode();
                oneTimeCodeTextView.setText(oneTimeCode+"");
            } else {
                codeEntrance.setVisibility(View.VISIBLE);
                codeDisplay.setVisibility(View.GONE);
                oneTimeCodeEditText.requestFocus();
            }
        }catch (Exception e){
            toastAndAlertBuilder.makeSimpleToast("همکار گرامی خطا رخ داد"+"\n"+e.getMessage());
        }
    }
    private void setCancelClickListener(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void setRegisterClickListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (oneTimeCodeEditText.getText() == null || oneTimeCodeEditText.getText().toString().equals("")) {
                        toastAndAlertBuilder.makeSimpleToast("لطفاً کد را به درستی وارد فرمایید");
                        return;
                    }
                    String oneTimeCode = oneTimeCodeEditText.getText().toString();
                    sharedPreferenceManager.put(sharedPreferanceKey, oneTimeCode);
                    sharedPreferenceManager.apply();
                    toastAndAlertBuilder.makeSimpleToast("کد با موفقیت ثبت شد");
                } catch (Exception e) {
                    toastAndAlertBuilder.makeSimpleToast("همکار گرامی خطا رخ داد" + "\n" + e.getMessage());
                }
            }
        });
    }
    private boolean isOneTimeCodeEntered() {
        String oneTimeCode = sharedPreferenceManager.getString(sharedPreferanceKey);
        return oneTimeCode != "";
    }
    private int getOneTimeCode(){
        String mamoorCode = sharedPreferenceManager.getString(sharedPreferanceKey);
        int month=DateAndTime.getPersianCurrentMonth();
        int day=DateAndTime.getPersianCurrentDay();
        int oneTimeCode= Integer.parseInt(mamoorCode)+(month*day*1313);
        return oneTimeCode;
    }
}
