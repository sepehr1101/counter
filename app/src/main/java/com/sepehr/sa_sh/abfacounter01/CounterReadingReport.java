package com.sepehr.sa_sh.abfacounter01;

import com.orm.SugarRecord;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sa-sh on 8/11/2016.
 */
public class CounterReadingReport extends SugarRecord {
    String billId;
    String serial;
    Integer reportCode;
    String reportTitle;
    Integer userCode;
    String saveTimeStamp;
    Integer offLoadState=0;
    BigDecimal trackNumber;

    public CounterReadingReport() {
    }

    public CounterReadingReport(String billId, String serial, Integer reportCode,
                                String reportTitle, Integer userCode,int offLoadState,BigDecimal trackNumber) {
        this.billId = billId;
        this.serial = serial;
        this.reportCode = reportCode;
        this.reportTitle = reportTitle;
        this.userCode = userCode;
        this.saveTimeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.offLoadState=offLoadState;
        this.trackNumber=trackNumber;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getReportCode() {
        return reportCode;
    }

    public void setReportCode(Integer reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public String getSaveTimeStamp() {
        return saveTimeStamp;
    }

    public void setSaveTimeStamp(String saveTimeStamp) {
        this.saveTimeStamp = saveTimeStamp;
    }

    public Integer getOffLoadState() {
        return offLoadState;
    }

    public void setOffLoadState(Integer offLoadState) {
        this.offLoadState = offLoadState;
    }
}
