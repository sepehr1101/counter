package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.OnOffLoadViewModel;

import java.math.BigDecimal;

/**
 * Created by saeid on 3/30/2017.
 */
public class OnOffLoadModel extends SugarRecord{
    public String s_id;
    public Integer _index;
    public Integer offLoadState;//vaziat e taxlie shodan , nashodan , ...

    //region OnLoad members
    public int zoneId;
    public String listNumber;
    public BigDecimal trackNumber;
    public String billId;
    public BigDecimal radif;
    public String eshterak;
    public String qeraatCode;
    public String name;
    public String family;
    public String address;
    public Integer karbariCod;
    public Integer tedadMaskooni;
    public Integer tedadNonMaskooni;
    public Integer tedadKol;
    public Integer qotr;
    public Integer sifoonQotr;
    public Integer preNumber;
    public Float preAverage;
    public String preDate;
    public String preDateMiladi;
    public Integer preCounterState;
    public Integer vaziatCode;
    public String counterSerial;
    public Integer zarfiat;
    public Integer ahadMasraf;
    public String tavizDate;
    //endregion

    //region Offload members
    public Integer counterNumber;
    public Integer counterStateCode;
    public Integer possibleAddressl;
    public String possibleAddress;//*new column 21 in assets
    public String possibleCounterSerial;
    public String possibleEshterak;
    public String possibleMobile;
    public String possiblePhoneNumber;
    public String possibleTedadMaskooni;
    public String possibleTedadTejari;
    public String possibleKarbariCode;
    public BigDecimal latitude;
    public BigDecimal longitude;
    public Integer gisAccuracy;
    public Integer cameraState;
    public Integer masrafState;
    public Integer masraf;
    public Float newRate;
    public Integer dateDifference;
    public Boolean isCounterNumberShown;
    public Integer highLowState;
    public Integer tedadKhali;
    public Integer offloadedCount;
    //
    public String registerDate;
    public String registerDateJalali;
    public Integer counterStatePosition;
    public String description;
    //
    //endregion

    //region constructors


    public OnOffLoadModel() {
    }

    public OnOffLoadModel(OnOffLoadViewModel onOffLoadViewModel,int i) {
        this.setS_id(onOffLoadViewModel.Id.toString());
        this.setAddress(onOffLoadViewModel.OnLoad.Address);
        this.setAhadMasraf(onOffLoadViewModel.OnLoad.AhadMasraf);
        this.setBillId(onOffLoadViewModel.OnLoad.BillId);
        this.setCameraState(onOffLoadViewModel.OffLoad.CameraState);
        this.setCounterNumber(onOffLoadViewModel.OffLoad.CounterNumber);
        this.setCounterSerial(onOffLoadViewModel.OnLoad.CounterSerial);
        this.setCounterStateCode(onOffLoadViewModel.OffLoad.CounterStateCode);
        this.setDateDifference(onOffLoadViewModel.OffLoad.DateDifference);
        this.setEshterak(onOffLoadViewModel.OnLoad.Eshterak);
        this.setFamily(onOffLoadViewModel.OnLoad.Family);
        this.setGisAccuracy(onOffLoadViewModel.OffLoad.GisAccuracy);
        this.setHighLowState(onOffLoadViewModel.OffLoad.HighLowState);
        this.setIndex(i);
        this.setIsCounterNumberShown(false);
        this.setKarbariCod(onOffLoadViewModel.OnLoad.KarbariCod);
        this.setLatitude(onOffLoadViewModel.OffLoad.Latitude);
        this.setLongitude(onOffLoadViewModel.OffLoad.Longitude);
        this.setListNumber(onOffLoadViewModel.OnLoad.ListNumber);
        this.setMasraf(onOffLoadViewModel.OffLoad.Masraf);
        this.setMasrafState(onOffLoadViewModel.OffLoad.MasrafState);
        this.setNewRate(onOffLoadViewModel.OffLoad.NewRate);
        this.setTavizDate(onOffLoadViewModel.OnLoad.TavizDate);
        this.setOffloadedCount(onOffLoadViewModel.OffLoad.OffloadedCount);
        this.setName(onOffLoadViewModel.OnLoad.Name);
        this.setPossibleAddress(onOffLoadViewModel.OffLoad.PossibleAddress);
        this.setPossibleCounterSerial(onOffLoadViewModel.OffLoad.PossibleCounterSerial);
        this.setPossibleEshterak(onOffLoadViewModel.OffLoad.PossibleEshterak);
        this.setPossibleKarbariCode(onOffLoadViewModel.OffLoad.PossibleKarbariCode);
        this.setPossibleMobile(onOffLoadViewModel.OffLoad.PossibleMobile);
        this.setPossiblePhoneNumber(onOffLoadViewModel.OffLoad.PossiblePhoneNumber);
        this.setPossibleTedadMaskooni(onOffLoadViewModel.OffLoad.PossibleTedadMaskooni);
        this.setPossibleTedadTejari(onOffLoadViewModel.OffLoad.PossibleTedadTejari);
        this.setPreAverage(onOffLoadViewModel.OnLoad.PreAverage);
        this.setPreCounterState(onOffLoadViewModel.OnLoad.PreCounterState);
        this.setPreDate(onOffLoadViewModel.OnLoad.PreDate);
        this.setPreDateMiladi(onOffLoadViewModel.OnLoad.PreDateMiladi);
        this.setPreNumber(onOffLoadViewModel.OnLoad.PreNumber);
        this.setQeraatCode(onOffLoadViewModel.OnLoad.QeraatCode);
        this.setQotr(onOffLoadViewModel.OnLoad.Qotr);
        this.setSifoonQotr(onOffLoadViewModel.OnLoad.SifoonQotr);
        this.setRadif(onOffLoadViewModel.OnLoad.Radif);
        this.setTedadKhali(onOffLoadViewModel.OffLoad.TEDAD_KHALI);
        this.setTedadKol(onOffLoadViewModel.OnLoad.TedadKol);
        this.setTedadMaskooni(onOffLoadViewModel.OnLoad.TedadMaskooni);
        this.setTedadNonMaskooni(onOffLoadViewModel.OnLoad.TedadNonMaskooni);
        this.setTrackNumber(onOffLoadViewModel.OnLoad.TrackNumber);
        this.setVaziatCode(onOffLoadViewModel.OnLoad.VaziatCode);
        this.setZarfiat(onOffLoadViewModel.OnLoad.Zarfiat);
        this.setZoneId(onOffLoadViewModel.OnLoad.ZoneId);
        this.setCounterStatePosition(onOffLoadViewModel.OffLoad.CounterStatePosition);
    }

    //endregion

    //region setter and getter
    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }
    public Integer getIndex() {
        return _index;
    }

    public void setIndex(Integer index) {
        this._index = index;
    }

    public Integer getOffLoadState() {
        return this.offLoadState==null?new Integer(0):this.offLoadState;
    }

    public void setOffLoadState(Integer offLoadState) {
        this.offLoadState = offLoadState;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public BigDecimal getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(BigDecimal trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public BigDecimal getRadif() {
        return radif;
    }

    public void setRadif(BigDecimal radif) {
        this.radif = radif;
    }

    public String getEshterak() {
        return eshterak;
    }

    public void setEshterak(String eshterak) {
        this.eshterak = eshterak;
    }

    public String getQeraatCode() {
        return this.qeraatCode==null?eshterak:this.qeraatCode;
    }

    public void setQeraatCode(String qeraatCode) {
        this.qeraatCode = qeraatCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getKarbariCod() {
        return karbariCod;
    }

    public void setKarbariCod(Integer karbariCod) {
        this.karbariCod = karbariCod;
    }

    public Integer getTedadMaskooni() {
        return this.tedadMaskooni==null?new Integer(0):this.tedadMaskooni;
    }

    public void setTedadMaskooni(Integer tedadMaskooni) {
        this.tedadMaskooni = tedadMaskooni;
    }

    public Integer getTedadNonMaskooni() {
        return tedadNonMaskooni;
    }

    public void setTedadNonMaskooni(Integer tedadNonMaskooni) {
        this.tedadNonMaskooni = tedadNonMaskooni;
    }

    public Integer getTedadKol() {
        return  tedadKol;
    }

    public void setTedadKol(Integer tedadKol) {
        this.tedadKol = tedadKol;
    }

    public Integer getQotr() {
        return qotr;
    }

    public void setQotr(Integer qotr) {
        this.qotr = qotr;
    }

    public Integer getSifoonQotr() {
        return sifoonQotr;
    }

    public void setSifoonQotr(Integer sifoonQotr) {
        this.sifoonQotr = sifoonQotr;
    }

    public Integer getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(Integer preNumber) {
        this.preNumber = preNumber;
    }

    public Float getPreAverage() {
        return preAverage;
    }

    public void setPreAverage(Float preAverage) {
        this.preAverage = preAverage;
    }

    public String getPreDate() {
        return preDate;
    }

    public void setPreDate(String preDate) {
        this.preDate = preDate;
    }

    public String getPreDateMiladi() {
        return preDateMiladi;
    }

    public void setPreDateMiladi(String preDateMiladi) {
        this.preDateMiladi = preDateMiladi;
    }

    public Integer getPreCounterState() {
        return preCounterState;
    }

    public void setPreCounterState(Integer preCounterState) {
        this.preCounterState = preCounterState;
    }

    public Integer getVaziatCode() {
        return this.vaziatCode==null?new Integer(0):this.vaziatCode;
    }

    public void setVaziatCode(Integer vaziatCode) {
        this.vaziatCode = vaziatCode;
    }

    public String getCounterSerial() {
        return this.counterSerial==null?"":this.counterSerial;
    }

    public void setCounterSerial(String counterSerial) {
        this.counterSerial = counterSerial;
    }

    public Integer getZarfiat() {
        return this.zarfiat==null?new Integer(0):this.zarfiat;
    }

    public void setZarfiat(Integer zarfiat) {
        this.zarfiat = zarfiat;
    }

    public Integer getAhadMasraf() {
        return this.ahadMasraf==null?new Integer(0):this.ahadMasraf;
    }

    public void setAhadMasraf(Integer ahadMasraf) {
        this.ahadMasraf = ahadMasraf;
    }

    public String getTavizDate() {
        return this.tavizDate==null?"":this.tavizDate;
    }

    public void setTavizDate(String tavizDate) {
        this.tavizDate = tavizDate;
    }

    public Integer getCounterNumber() {
        return this.counterNumber;
    }

    public void setCounterNumber(Integer counterNumber) {
        this.counterNumber = counterNumber;
    }

    public Integer getCounterStateCode() {
        return this.counterStateCode==null?new Integer(0):this.counterStateCode;
    }

    public void setCounterStateCode(Integer counterStateCode) {
        this.counterStateCode = counterStateCode;
    }

    public String getPossibleAddress() {
        return this.possibleAddress==null?"":this.possibleAddress;
    }

    public void setPossibleAddress(String possibleAddress) {
        this.possibleAddress = possibleAddress;
    }

    public String getPossibleCounterSerial() {
        return this.possibleCounterSerial==null ? "" :this.possibleCounterSerial;
    }

    public void setPossibleCounterSerial(String possibleCounterSerial) {
        this.possibleCounterSerial = possibleCounterSerial;
    }

    public String getPossibleEshterak() {
        return this.possibleEshterak==null ?"" :this.possibleEshterak;
    }

    public void setPossibleEshterak(String possibleEshterak) {
        this.possibleEshterak = possibleEshterak;
    }

    public String getPossibleMobile() {
        return this.possibleMobile==null ?"" :this.possibleMobile;
    }

    public void setPossibleMobile(String possibleMobile) {
        this.possibleMobile = possibleMobile;
    }

    public String getPossiblePhoneNumber() {
        return this.possiblePhoneNumber==null ?"":this.possiblePhoneNumber;
    }

    public void setPossiblePhoneNumber(String possiblePhoneNumber) {
        this.possiblePhoneNumber = possiblePhoneNumber;
    }

    public String getPossibleTedadMaskooni() {
        return this.possibleTedadMaskooni==null?"" :this.possibleTedadMaskooni;
    }

    public void setPossibleTedadMaskooni(String possibleTedadMaskooni) {
        this.possibleTedadMaskooni = possibleTedadMaskooni;
    }

    public String getPossibleTedadTejari() {
        return this.possibleTedadTejari==null ?"":this.possibleTedadTejari;
    }

    public void setPossibleTedadTejari(String possibleTedadTejari) {
        this.possibleTedadTejari = possibleTedadTejari;
    }

    public String getPossibleKarbariCode() {
        return this.possibleKarbariCode==null?"":this.possibleKarbariCode;
    }

    public void setPossibleKarbariCode(String possibleKarbariCode) {
        this.possibleKarbariCode = possibleKarbariCode;
    }

    public BigDecimal getLatitude() {
        return this.latitude==null?new BigDecimal(0):this.latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude==null?new BigDecimal(0):this.longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getGisAccuracy() {
        return this.gisAccuracy==null?new Integer(0):this.gisAccuracy;
    }

    public void setGisAccuracy(Integer gisAccuracy) {
        this.gisAccuracy = gisAccuracy;
    }

    public Integer getCameraState() {
        return this.cameraState==null?new Integer(0):this.cameraState;
    }

    public void setCameraState(Integer cameraState) {
        this.cameraState = cameraState;
    }

    public Integer getMasrafState() {
        return this.masrafState==null?new Integer(0):this.masrafState;
    }

    public void setMasrafState(Integer masrafState) {
        this.masrafState = masrafState;
    }

    public Integer getMasraf() {
        return this.masraf==null?new Integer(0):this.masraf;
    }

    public void setMasraf(Integer masraf) {
        this.masraf = masraf;
    }

    public Float getNewRate() {
        return this.newRate==null ?new Float(0):this.newRate;
    }

    public void setNewRate(Float newRate) {
        this.newRate = newRate;
    }

    public Integer getDateDifference() {
        return this.dateDifference==null?new Integer(0):this.dateDifference;
    }

    public void setDateDifference(Integer dateDifference) {
        this.dateDifference = dateDifference;
    }

    public Boolean getIsCounterNumberShown() {
        return this.isCounterNumberShown==null?new Boolean(false):this.isCounterNumberShown;
    }

    public void setIsCounterNumberShown(Boolean isCounterNumberShown) {
        this.isCounterNumberShown = isCounterNumberShown;
    }

    public Integer getHighLowState() {
        return this.highLowState==null?new Integer(0):this.highLowState;
    }

    public void setHighLowState(Integer highLowState) {
        this.highLowState = highLowState;
    }

    public Integer getTedadKhali() {
        return this.tedadKhali==null?new Integer(0):this.tedadKhali;
    }

    public void setTedadKhali(Integer tedadKhali) {
        this.tedadKhali = tedadKhali;
    }

    public Integer getOffloadedCount() {
        return this.offloadedCount==null?new Integer(0):this.offloadedCount;
    }

    public void setOffloadedCount(Integer offloadedCount) {
        this.offloadedCount = offloadedCount;
    }

    public String getRegisterDate() {
        return this.registerDate==null?"":this.registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getRegisterDateJalali() {
        return this.registerDateJalali==null?"":this.registerDateJalali;
    }

    public void setRegisterDateJalali(String registerDateJalali) {
        this.registerDateJalali = registerDateJalali;
    }

    public Integer getCounterStatePosition() {
        return this.counterStatePosition==null?new Integer(0):this.counterStatePosition;
    }

    public void setCounterStatePosition(Integer counterStatePosition) {
        this.counterStatePosition = counterStatePosition;
    }

    public String getDescription() {
        return description==null?"":description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //endregion
}
