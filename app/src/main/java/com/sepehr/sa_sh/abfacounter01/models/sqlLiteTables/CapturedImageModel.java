package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sa-sh on 8/6/2016.
 */
public class CapturedImageModel extends SugarRecord{
    String billId;
    Integer savedByUserCode;
    String saveTimeStamp;
    String path;
    BigDecimal trackNumber;
    Integer status;
    String oId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public CapturedImageModel() {

    }
    //

    public CapturedImageModel(String billId, Integer savedByUserCode, String path,BigDecimal trackNumber,int status,String onOffloaId) {
        this.billId = billId;
        this.savedByUserCode = savedByUserCode;
        this.saveTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.path = path;
        this.trackNumber=trackNumber;
        this.status=status;
        this.oId =onOffloaId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Integer getSavedByUserCode() {
        return savedByUserCode;
    }

    public void setSavedByUserCode(Integer savedByUserCode) {
        this.savedByUserCode = savedByUserCode;
    }

    public String getSaveTimeStamp() {
        return saveTimeStamp;
    }

    public void setSaveTimeStamp(String saveTimeStamp) {
        this.saveTimeStamp = saveTimeStamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BigDecimal getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(BigDecimal trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getOnOffloaId() {
        return oId;
    }
    public void setOnOffloaId(String onOffloaId) {
        this.oId = onOffloaId;
    }
}
