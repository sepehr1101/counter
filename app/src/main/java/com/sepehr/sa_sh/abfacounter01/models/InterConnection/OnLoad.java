package com.sepehr.sa_sh.abfacounter01.models.InterConnection;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by saeid on 3/30/2017.
 */
public class OnLoad {
    public int ZoneId;
    public String ListNumber;
    public BigDecimal TrackNumber;
    public String BillId;
    public BigDecimal Radif;
    public String Eshterak;
    public String QeraatCode;
    public String Name;
    public String Family;
    public String Address;
    public Integer KarbariCod;
    public Integer TedadMaskooni;
    public Integer TedadNonMaskooni;
    public Integer TedadKol;
    public Integer Qotr;
    public Integer SifoonQotr;
    public Integer PreNumber;
    public Float PreAverage;
    public String PreDate;
    public String PreDateMiladi;
    public Integer PreCounterState;
    public Integer VaziatCode;
    public String CounterSerial;
    public Integer Zarfiat;
    public Integer AhadMasraf;
    public String TavizDate;

    public OnLoad() {
    }

    public OnLoad(OnOffLoadModel onOffLoadModel) {
        this.Address=onOffLoadModel.getAddress();
        this.AhadMasraf=onOffLoadModel.getAhadMasraf();
        this.BillId=onOffLoadModel.getBillId();
        this.CounterSerial=onOffLoadModel.getCounterSerial();
        this.Eshterak=onOffLoadModel.getEshterak();
        this.Family=onOffLoadModel.getFamily();
        this.KarbariCod=onOffLoadModel.getKarbariCod();
        this.ListNumber=onOffLoadModel.getListNumber();
        this.Name=onOffLoadModel.getName();
        this.PreAverage=onOffLoadModel.getPreAverage();
        this.PreCounterState=onOffLoadModel.getPreCounterState();
        this.PreDate=onOffLoadModel.getPreDate();
        this.PreDateMiladi=onOffLoadModel.getPreDateMiladi();
        this.PreNumber=onOffLoadModel.getPreNumber();
        this.QeraatCode=onOffLoadModel.getQeraatCode();
        this.Qotr=onOffLoadModel.getQotr();
        this.Radif=onOffLoadModel.getRadif();
        this.SifoonQotr=onOffLoadModel.getSifoonQotr();
        this.TavizDate=onOffLoadModel.getTavizDate();
        this.TedadKol=onOffLoadModel.tedadKol;
        this.TedadMaskooni=onOffLoadModel.getTedadMaskooni();
        this.TedadNonMaskooni=onOffLoadModel.getTedadNonMaskooni();
        this.TrackNumber=onOffLoadModel.getTrackNumber();
        this.VaziatCode=onOffLoadModel.getVaziatCode();
        this.Zarfiat=onOffLoadModel.getZarfiat();
        this.ZoneId=onOffLoadModel.getZoneId();
    }
}
