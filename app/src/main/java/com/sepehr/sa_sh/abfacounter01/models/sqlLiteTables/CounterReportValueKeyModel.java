package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.CounterReportValueKeyViewModel;

/**
 * Created by saeid on 3/10/2017.
 */
public class CounterReportValueKeyModel extends SugarRecord{
    int CODE;
    String TITLE;
    boolean IS_AHAD;
    boolean IS_KARBARI;
    boolean CAN_NUMBER_BE_LESS_THAN_PRE;
    boolean IS_TAVIZI;
    boolean IS_ACTIVE;

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public boolean IS_AHAD() {
        return IS_AHAD;
    }

    public void setIS_AHAD(boolean IS_AHAD) {
        this.IS_AHAD = IS_AHAD;
    }

    public boolean IS_KARBARI() {
        return IS_KARBARI;
    }

    public void setIS_KARBARI(boolean IS_KARBARI) {
        this.IS_KARBARI = IS_KARBARI;
    }

    public boolean isCAN_NUMBER_BE_LESS_THAN_PRE() {
        return CAN_NUMBER_BE_LESS_THAN_PRE;
    }

    public void setCAN_NUMBER_BE_LESS_THAN_PRE(boolean CAN_NUMBER_BE_LESS_THAN_PRE) {
        this.CAN_NUMBER_BE_LESS_THAN_PRE = CAN_NUMBER_BE_LESS_THAN_PRE;
    }

    public boolean IS_TAVIZI() {
        return IS_TAVIZI;
    }

    public void setIS_TAVIZI(boolean IS_TAVIZI) {
        this.IS_TAVIZI = IS_TAVIZI;
    }

    public boolean IS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(boolean IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public CounterReportValueKeyModel() {
    }

    public CounterReportValueKeyModel(CounterReportValueKeyViewModel counterReportViewModel) {
        this.setCODE(counterReportViewModel.getCode());
        this.setTITLE(counterReportViewModel.getTitle());
        this.setIS_AHAD(counterReportViewModel.isAhad());
        this.setIS_KARBARI(counterReportViewModel.isKarbari());
        this.setCAN_NUMBER_BE_LESS_THAN_PRE(counterReportViewModel.isCanNumberBeLessThanPre());
        this.setIS_TAVIZI(counterReportViewModel.isTavizi());
        this.setIS_ACTIVE(counterReportViewModel.isActive());
    }
}
