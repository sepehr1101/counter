package com.sepehr.sa_sh.abfacounter01.models.InterConnection;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;

/**
 * Created by saeid on 3/30/2017.
 */
public class OffLoad {
    public Integer CounterNumber;
    public Integer CounterStateCode;
    public String PossibleAddress;
    public String PossibleCounterSerial;
    public String PossibleEshterak;
    public String PossibleMobile;
    public String PossiblePhoneNumber;
    public String PossibleTedadMaskooni;
    public String PossibleTedadTejari;
    public String PossibleKarbariCode;
    public BigDecimal Latitude;
    public BigDecimal Longitude;
    public Integer GisAccuracy;
    public Integer CameraState;
    public Integer MasrafState;
    public Integer Masraf;
    public Float NewRate;
    public Integer DateDifference;
    public Boolean IS_COUNTER_NUMBER_SHOWN;
    public Integer HighLowState;
    public Integer TEDAD_KHALI;
    public Integer OffloadedCount;
    public Integer CounterStatePosition;

    public OffLoad() {
    }

    public OffLoad(OnOffLoadModel onOffLoadModel) {
        this.CameraState=onOffLoadModel.getCameraState();
        this.CounterNumber=onOffLoadModel.getCounterNumber();
        this.CounterStateCode=onOffLoadModel.getCounterStateCode();
        this.DateDifference=onOffLoadModel.getDateDifference();
        this.GisAccuracy=onOffLoadModel.getGisAccuracy();
        this.HighLowState=onOffLoadModel.getHighLowState();
        this.IS_COUNTER_NUMBER_SHOWN=onOffLoadModel.getIsCounterNumberShown();
        this.Latitude=onOffLoadModel.getLatitude();
        this.Longitude=onOffLoadModel.getLongitude();
        this.Masraf=onOffLoadModel.getMasraf();
        this.MasrafState=onOffLoadModel.getMasrafState();
        this.NewRate=onOffLoadModel.newRate;
        this.OffloadedCount=onOffLoadModel.offloadedCount;
        this.PossibleAddress=onOffLoadModel.getPossibleAddress();
        this.PossibleCounterSerial=onOffLoadModel.getPossibleCounterSerial();
        this.PossibleEshterak=onOffLoadModel.getPossibleEshterak();
        this.PossibleKarbariCode=onOffLoadModel.getPossibleKarbariCode();
        this.PossibleMobile=onOffLoadModel.getPossibleMobile();
        this.PossiblePhoneNumber=onOffLoadModel.getPossiblePhoneNumber();
        this.PossibleTedadMaskooni=onOffLoadModel.getPossibleTedadMaskooni();
        this.PossibleTedadTejari=onOffLoadModel.getPossibleTedadTejari();
        this.TEDAD_KHALI=onOffLoadModel.getTedadKhali();
        this.CounterStatePosition=onOffLoadModel.counterStatePosition;
    }
}
