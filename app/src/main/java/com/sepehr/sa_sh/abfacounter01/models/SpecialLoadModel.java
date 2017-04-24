package com.sepehr.sa_sh.abfacounter01.models;

import java.math.BigDecimal;

/**
 * Created by saeid on 11/9/2016.
 */
public class SpecialLoadModel {
    String BillId;
    BigDecimal Radif;
    String Eshterak;
    String Name;
    String Family;
    String Address;
    Integer KarbariCod;
    Integer TedadMaskooni;
    Integer TedadNonMaskooni;
    Integer PreNumber;
    BigDecimal Average;
    String PreDate;
    String ListNumber;
    BigDecimal TrackNumber;
    BigDecimal Qotr;
    //
    BigDecimal CounterNumber;
    BigDecimal CounterStateCode;
    BigDecimal Latitude;
    BigDecimal Longitude;
    String PossiblePhoneNumber;
    String PossibleMobile;
    String PossibleAddress;
    String possibleCounterSerial;
    Integer possibleQotrCode;
    String  possibleEshterak;
    Integer possibleTedadMaskooni;
    Integer possibleTedadTejari;
    Integer possibleKarbariCode;
    Integer GisAccuracy;

    public String getBillId() {
        return BillId;
    }

    public BigDecimal getRadif() {
        return Radif;
    }

    public String getEshterak() {
        return Eshterak;
    }

    public String getName() {
        return Name;
    }

    public String getFamily() {
        return Family;
    }

    public String getAddress() {
        return Address;
    }

    public Integer getKarbariCod() {
        return KarbariCod;
    }

    public Integer getTedadMaskooni() {
        return TedadMaskooni;
    }

    public Integer getTedadNonMaskooni() {
        return TedadNonMaskooni;
    }

    public Integer getPreNumber() {
        return PreNumber;
    }

    public BigDecimal getAverage() {
        return Average;
    }

    public String getPreDate() {
        return PreDate;
    }

    public String getListNumber() {
        return ListNumber;
    }

    public BigDecimal getTrackNumber() {
        return TrackNumber;
    }

    public BigDecimal getQotr() {
        return Qotr;
    }

    public BigDecimal getCounterNumber() {
        return CounterNumber;
    }

    public BigDecimal getCounterStateCode() {
        return CounterStateCode;
    }

    public BigDecimal getLatitude() {
        return Latitude;
    }

    public BigDecimal getLongitude() {
        return Longitude;
    }

    public String getPossiblePhoneNumber() {
        return PossiblePhoneNumber;
    }

    public String getPossibleMobile() {
        return PossibleMobile;
    }

    public String getPossibleAddress() {
        return PossibleAddress;
    }

    public String getPossibleCounterSerial() {
        return possibleCounterSerial;
    }

    public Integer getPossibleQotrCode() {
        return possibleQotrCode;
    }

    public String getPossibleEshterak() {
        return possibleEshterak;
    }

    public Integer getPossibleTedadMaskooni() {
        return possibleTedadMaskooni;
    }

    public Integer getPossibleTedadTejari() {
        return possibleTedadTejari;
    }

    public Integer getPossibleKarbariCode() {
        return possibleKarbariCode;
    }

    public Integer getGisAccuracy() {
        return GisAccuracy;
    }
}
