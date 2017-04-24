package com.sepehr.sa_sh.abfacounter01;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.OnOffLoadStatic;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by sa-sh on 8/15/2016.
 */
public class TaqikAhadFragment extends DialogFragment {

    EditText ahadMaskooni,ahadNonMaskooni;
    String billId;
    Integer possibleMaskooni,possibleNonMaskooni;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_taqir_ahad, container, false);
        dismissDialog(rootView);
        confirmTaqirAhad(rootView);
        ahadMaskooni=(EditText)rootView.findViewById(R.id.ahahMaskooni);
        ahadNonMaskooni=(EditText)rootView.findViewById(R.id.ahadNonMaskooni);
        ahadMaskooni.setText("");
        ahadNonMaskooni.setText("");
        billId=((DisplayViewPager)getActivity()).getBill_id();
        setAhadTextIfExist();
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
    //
    private void dismissDialog(View rootView){
        Button dismissCounterReport = (Button) rootView.findViewById(R.id.dismissTaqirAhad);
        dismissCounterReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //
    private  void confirmTaqirAhad(View rootView){
        Button confirmButton=(Button)rootView.findViewById(R.id.okTaqirAhad);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTaqirAhad();
            }
        });
    }
    //
    private void doTaqirAhad(){
        possibleMaskooni= ahadMaskooni.getText().toString().equals("") ?0:  new Integer(ahadMaskooni.getText().toString());
        possibleNonMaskooni=ahadNonMaskooni.getText().toString().equals("") ?0: new Integer(ahadNonMaskooni.getText().toString());
        OnOffLoadModel model=OnOffLoadModel.find(OnOffLoadModel.class,
                "BILL_ID= ? ",billId).get(0);
        model.possibleTedadMaskooni=possibleMaskooni+"";
        model.possibleTedadTejari=possibleNonMaskooni+"";
        model.save();
        Toast.makeText(getContext(), "تعداد آحاد ذخیره شد",
                Toast.LENGTH_SHORT).show();
        dismiss();
    }
    //
    private void setAhadTextIfExist(){
        OnOffLoadModel model= OnOffLoadStatic.GetOnOfLoad(billId);
        if(model.possibleTedadMaskooni!=null){
            ahadMaskooni.setText(model.possibleTedadMaskooni.toString());
        }
        if(model.possibleTedadTejari!=null){
            ahadNonMaskooni.setText(model.possibleTedadTejari.toString());
        }
    }
}
