package com.sepehr.sa_sh.abfacounter01.models;

/**
 * Created by saeid on 3/10/2017.
 */
public class ReportCheckboxModel {
    int reportCode;
    String TITLE;
    boolean isChecked;

    public ReportCheckboxModel() {
    }

    public ReportCheckboxModel(int reportCode, String TITLE, boolean isChecked) {
        this.reportCode = reportCode;
        this.TITLE = TITLE;
        this.isChecked = isChecked;
    }

    public int getReportCode() {
        return reportCode;
    }

    public void setReportCode(int reportCode) {
        this.reportCode = reportCode;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
