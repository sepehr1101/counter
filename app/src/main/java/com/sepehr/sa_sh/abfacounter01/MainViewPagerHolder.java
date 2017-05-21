package com.sepehr.sa_sh.abfacounter01;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICounterStateService;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

/**
 * Created by saeid on 3/30/2017.
 */
public class MainViewPagerHolder {
   public TextView shomareBadane;
    public Button preNumber;
    public TextView preDate;
    public TextView qotr;
    public TextView qotrSifoon;
    public TextView billId;
    public TextView radif;
    public TextView firstLastName;
    public TextView eshterak;
    public ExpandableTextView address;
    public EditText counterNumber;
    public TextView tedadMaskooni;
    public TextView tedadTejari;
    public TextView karbari;
    public TextView tedadKol;
    public TextView addressExpandable;
    public Spinner counterStateSpinner;
    public TextView trackNumber;

    public MainViewPagerHolder(View itemView) {
        shomareBadane=(TextView)itemView.findViewById(R.id.shomareBadane);
        preNumber=(Button)itemView.findViewById(R.id.preNumber);
        preDate=(TextView)itemView.findViewById(R.id.preDate);
        qotr=(TextView)itemView.findViewById(R.id.qotr);
        qotrSifoon=(TextView) itemView.findViewById(R.id.qotrSifoon);
        billId = (TextView) itemView.findViewById(R.id.billId);
        radif=(TextView)itemView.findViewById(R.id.radif);
        firstLastName = (TextView) itemView.findViewById(R.id.firstLastName);
        karbari=(TextView) itemView.findViewById(R.id.karbari);
        eshterak=(TextView) itemView.findViewById(R.id.eshterak);
        address=(ExpandableTextView) itemView.findViewById(R.id.address);
        counterStateSpinner = (Spinner)itemView.findViewById(R.id.spinnerCounterState);
        counterNumber=(EditText)itemView.findViewById(R.id.counterNumberEditText);
        tedadMaskooni=(TextView)itemView.findViewById(R.id.tedadMaskooni);
        tedadTejari=(TextView)itemView.findViewById(R.id.tedadTejari);
        tedadKol=(TextView) itemView.findViewById(R.id.tedadKol);
        addressExpandable=(TextView)itemView.findViewById(R.id.expandable_text);
        //
        trackNumber=(TextView)itemView.findViewById(R.id.trackNumber);
    }

    public void SetTextAndConfig(OnOffLoadModel onOffLoad,ReadingConfigModel readingConfig,
                                 ICounterStateService counterStateService,String[] spinnerItems,
                                 String[] qotrList,String[] qotrSifoonList,String karbariTitle){
        this.shomareBadane.setText(onOffLoad.getCounterSerial() == null ? "اعلام نشده" : onOffLoad.counterSerial);
        this.preNumber.setText(!onOffLoad.isCounterNumberShown ? "رقم قبلی":onOffLoad.getPreNumber()+"");
        String preHelper = onOffLoad.getPreDate() == null ? "      " : onOffLoad.getPreDate();
        this.preDate.setText(preHelper.substring(0, 2) + "/" + preHelper.substring(2, 4) + "/" + preHelper.substring(4, 6));

        this.qotr.setText(qotrList[onOffLoad.getQotr()]);
        onOffLoad.sifoonQotr=onOffLoad.sifoonQotr==null?0 :onOffLoad.sifoonQotr;
        this.qotrSifoon.setText(qotrSifoonList[onOffLoad.getSifoonQotr()]);
        this.billId.setText(onOffLoad.billId);
        this.radif.setText(onOffLoad.getRadif().setScale(0)+"");
        // firstLastName.setText(firstLastNames.get(position).trim());

        this.firstLastName.setText(onOffLoad.getName().trim() + " "
                + onOffLoad.getFamily().trim());

        if(readingConfig.isOnQeraatCode() && onOffLoad.qeraatCode!=null){
            this.eshterak.setText(onOffLoad.getQeraatCode().trim());
        }
        else{
            this.eshterak.setText(onOffLoad.getEshterak().trim());
        }

        this.address.setText(onOffLoad.getAddress().trim());
        this.tedadMaskooni.setText(onOffLoad.getTedadMaskooni()+"");
        this.tedadTejari.setText(onOffLoad.getTedadNonMaskooni()+"");
        this.tedadKol.setText(onOffLoad.getTedadKol()+"");
        this.trackNumber.setText(onOffLoad.getTrackNumber().toString());
        //

        this.karbari.setText(karbariTitle);
        /*if (onOffLoad.getCounterNumber() != null) {
            this.counterNumber.setText(onOffLoad.getCounterNumber().toString());
        }*/
        //
        if (onOffLoad.getCounterStatePosition() != null) {
            this.counterStateSpinner.setSelection(onOffLoad.getCounterStatePosition());
        }
        //*config*
        if(!readingConfig.isPreNumber()){
            this.preNumber.setVisibility(View.GONE);
        }
        if(readingConfig.isPreNumber()){
            this.preNumber.setVisibility(View.VISIBLE);
        }

        //counter state service
        if (!counterStateService.shouldIEnterNumber(onOffLoad.getCounterStateCode()) &&
                        !counterStateService.canIEnterNumber(onOffLoad.getCounterStateCode()) /*&&
                        onOffLoad.counterNumber==null*/) {
            counterNumber.setText(spinnerItems[onOffLoad.getCounterStatePosition()]);
        }
        if((counterStateService.shouldIEnterNumber(onOffLoad.getCounterStateCode()) ||
                counterStateService.canIEnterNumber(onOffLoad.getCounterStateCode())) &&
                onOffLoad.getCounterNumber() !=null){
            counterNumber.setText(onOffLoad.getCounterNumber() + "");
        }
    }
}
