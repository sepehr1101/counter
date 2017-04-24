package com.sepehr.sa_sh.abfacounter01;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IKarbariRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.KarbariRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.OnOffLoadStatic;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariGroup;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sa-sh on 8/10/2016.
 */
public class TaqirKarbariFragment extends DialogFragment {
    List<String> karbariGroupTitles;
    List<String> karbariTitles=new ArrayList<>();
    TextView karbariLabel;
    public boolean isSpinner2Visible=false;
    Button okTaqirKarbari;
    Spinner spinnerKarbariGroup,spinnerKarbari;
    int karbariGroupId,karbariId;
    String bill_id;
    IKarbariRepo karbariRepo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_taqir_karbari, container, false);
        initizlize(rootView);
        return rootView;
    }
    //
    private void initizlize(View rootView){
        karbariLabel=(TextView)rootView.findViewById(R.id.karbariLabel);
        spinnerKarbariGroup=(Spinner) rootView.findViewById(R.id.spinnerKarbariGroup);
        spinnerKarbari=(Spinner) rootView.findViewById(R.id.spinnerKarbari);
        okTaqirKarbari=(Button)rootView.findViewById(R.id.okTaqirKarbari);
        bill_id=((DisplayViewPager)getActivity()).getBill_id();
        dismissDialog(rootView);
        confirmTaqirKarbari();

        karbariRepo=new KarbariRepo();
        karbariGroupTitles=karbariRepo.getKarbariGroupTitles();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_custom_item, karbariGroupTitles);
        adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        spinnerKarbariGroup.setAdapter(adapter);
        karbariChangeClickListener();
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
        Button dismissCounterReport = (Button) rootView.findViewById(R.id.dismissTaqirkarbari);
        dismissCounterReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //
    private void confirmTaqirKarbari(){
        okTaqirKarbari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    karbariId = (spinnerKarbari.getSelectedItemPosition() + 1);
                    String karbariTitle = spinnerKarbari.getSelectedItem().toString();
                    Karbari realKarbari = Karbari.find(Karbari.class, "KARBARI_TITLE= ? AND KARBARI_GROUPFK= ? ",
                            karbariTitle, karbariGroupId + "").get(0);
                    OnOffLoadModel model = OnOffLoadStatic.GetOnOfLoad(bill_id);
                    model.possibleKarbariCode = realKarbari.karbariCod+"";
                    model.save();
                    Toast.makeText(getContext(), "کاربری ذخیره شد",
                            Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                catch (Exception e){
                    Log.e("save error",e.getMessage());
                }
            }
        });
    }
    //
    private void karbariChangeClickListener(){
        spinnerKarbariGroup.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int i, long l) {
                int selectedItemPosition=spinnerKarbariGroup.getSelectedItemPosition();
                karbariGroupId=selectedItemPosition;
                karbariTitles.clear();
                karbariTitles=karbariRepo.getFilterdKarbaries(karbariGroupId);
                final ArrayAdapter<String> adapterKarbari = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_custom_item, karbariTitles);
                if(!isSpinner2Visible){
                    spinnerKarbari.setVisibility(View.VISIBLE);
                    karbariLabel.setVisibility(View.VISIBLE);
                    okTaqirKarbari.setVisibility(View.VISIBLE);
                    isSpinner2Visible=true;
                }
                else{
                    adapterKarbari.notifyDataSetChanged();
                }
                adapterKarbari.setDropDownViewResource(R.layout.spinner_custom_item);
                spinnerKarbari.setAdapter(adapterKarbari);
            }
        });
    }
}
