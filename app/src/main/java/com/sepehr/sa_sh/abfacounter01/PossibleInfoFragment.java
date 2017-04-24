package com.sepehr.sa_sh.abfacounter01;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by sa-sh on 8/18/2016.
 */
public class PossibleInfoFragment extends DialogFragment {
    String billId;
    Button dissmiss,ok;
    EditText possibleEhterak,possiblePhoneNumber,possibleMobile,
            possibleAddress,possibleCounterSerial,tedadKhali;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_possible_info, container, false);
        billId=((DisplayViewPager)getActivity()).getBill_id();
        initialEditTexts(rootView);
        confirm(rootView);
        dismissDialog(rootView);
        fillEditTextsIfExist();
        return rootView;
    }

    private void initialEditTexts(View rootView) {
        possibleAddress = (EditText) rootView.findViewById(R.id.possibleAddress);
        possibleCounterSerial = (EditText) rootView.findViewById(R.id.possibleCounterSerial);
        possibleEhterak = (EditText) rootView.findViewById(R.id.possibleEshterak);
        possibleMobile = (EditText) rootView.findViewById(R.id.possibleMobile);
        possiblePhoneNumber = (EditText) rootView.findViewById(R.id.possiblePhoneNumber);
        tedadKhali=(EditText)rootView.findViewById(R.id.tedadKhali);
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
    private void dismissDialog(View rootView){
        dissmiss = (Button) rootView.findViewById(R.id.dismiss);
        dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //
    private void confirm(View rootView){
        ok=(Button)rootView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePossibleInfo()) {
                    doConfirm();
                }
            }
        });
    }
    //
    private void doConfirm() {
        OnOffLoadModel model = OnOffLoadModel.find(OnOffLoadModel.class,
                "BILL_ID= ? ", billId).get(0);
        String tedadKhaliString=(tedadKhali.getText().length()>0 )?
                tedadKhali.getText().toString():"0";
        model.possibleEshterak = possibleEhterak.getText().toString();
        model.possiblePhoneNumber=possiblePhoneNumber.getText().toString();
        model.possibleMobile=possibleMobile.getText().toString();
        model.possibleCounterSerial=possibleCounterSerial.getText().toString();
        model.possibleAddress=possibleAddress.getText().toString();
        model.tedadKhali=new Integer( tedadKhaliString);
        model.save();
        Toast.makeText(getContext(), "اطلاعات ذخیره شد",
                Toast.LENGTH_SHORT).show();
        //dismiss();
    }
    //
    private void fillEditTextsIfExist(){
        OnOffLoadModel model=OnOffLoadModel.find(OnOffLoadModel.class,
                "BILL_ID= ? ",billId).get(0);
        if(model.possibleAddress!=null){
            possibleAddress.setText(model.possibleAddress);
        }
        if(model.possibleMobile!=null ){
            possibleMobile.setText(model.possibleMobile);
        }
        if(model.possiblePhoneNumber!=null ){
            possiblePhoneNumber.setText(model.possiblePhoneNumber);
        }
        if(model.possibleCounterSerial!=null ){
            possibleCounterSerial.setText(model.possibleCounterSerial);
        }
        if(model.possibleEshterak!=null ){
            possibleEhterak.setText(model.possibleEshterak);
        }
        if(model.tedadKhali!=null){
            tedadKhali.setText(model.tedadKhali.toString());
        }
    }
    //
    private boolean validatePossibleInfo(){
        boolean result=true;
        int eshterakLength= possibleEhterak.getText().length();
        int phoneNoLength=possiblePhoneNumber.getText().length();
        int mobileLength=possibleMobile.getText().length();
        int counterSerialLength=possibleCounterSerial.getText().length();
        int addressLength=possibleAddress.getText().length();
        int tedadKhaliLength=tedadKhali.getText().length();

        if(eshterakLength>0 && possibleEhterak.getText().length()>10){
            makeErrorToast("اشتراک عددی حداکثر 10 رقمی میباشد");
            result=false;
        }
        if(phoneNoLength>0 &&possiblePhoneNumber.getText().length()!=8){
            makeErrorToast("شماره تلفن ثابت عددی 8 رقمی است");
            result=false;
        }
        if(mobileLength>0&& possibleMobile.getText().length()!=11){
            makeErrorToast("شماره موبایل عددی 11 رقمی است");
            result=false;
        }
        if(counterSerialLength>0 && possibleCounterSerial.getText().length()>10){
            makeErrorToast("شماره بدنه کنتور عددی کمتر از 10 رقم میباشد");
            result=false;
        }
        if(addressLength>0 && possibleAddress.getText().length()<10){
            makeErrorToast("حداقل طول آدرس 10 کاراکتر میباشد");
            result= false;
        }
        if(tedadKhaliLength >0 &&
                (!TextUtils.isDigitsOnly(tedadKhali.getText() ))){
            makeErrorToast("تعداد آحاد خالی عددی است");
            result= false;
        }
        if(tedadKhaliLength>0 && tedadKhali.getText().length()>4){
            makeErrorToast("لطفا تعداد خالی از سکنه را اصلاح فرمایید.");
            result= false;
        }
        if( eshterakLength<1 && phoneNoLength<1 &&mobileLength<1 &&
            counterSerialLength<1 && addressLength<1 && tedadKhaliLength<1){
            makeErrorToast("لطفا دست کم یکی از موارد پیمایش را وارد نمایید.");
            result= false;
        }
        return result;
    }
    //
    private void makeErrorToast(String message){
        Toast.makeText(getContext(),
                message,
                Toast.LENGTH_LONG).show();
    }
}
