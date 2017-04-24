package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.CounterStateValueKeyViewModel;

/**
 * Created by saeid on 3/9/2017.
 */
public class CounterStateValueKeyModel extends SugarRecord{
    int Id_2;
    int MAIN_CODE;
    int POSSIBLE_CODE;
    String TITLE;
    int CLIENT_ORDER;
    boolean CAN_ENTER_NUMBER;
    Boolean IS_MANE;
    Boolean CAN_NUMBER_BE_LESS_THAN_PRE;
    boolean IS_TAVIZI;
    boolean SHOULD_ENTER_NUMBER;

    public boolean IsTavizi() {
        return IS_TAVIZI;
    }

    public void setIS_TAVIZI(boolean IS_TAVIZI) {
        this.IS_TAVIZI = IS_TAVIZI;
    }

    public boolean CanEnterNumber() {
        return CAN_ENTER_NUMBER;
    }

    public void setCAN_ENTER_NUMBER(boolean CAN_ENTER_NUMBER) {
        this.CAN_ENTER_NUMBER = CAN_ENTER_NUMBER;
    }

    public Boolean IsMane() {
        return IS_MANE;
    }

    public void setIS_MANE(Boolean IS_MANE) {
        this.IS_MANE = IS_MANE;
    }

    public Boolean CanBeNumberLessThanPre() {
        return CAN_NUMBER_BE_LESS_THAN_PRE;
    }

    public void setCAN_NUMBER_BE_LESS_THAN_PRE(Boolean CAN_NUMBER_BE_LESS_THAN_PRE) {
        this.CAN_NUMBER_BE_LESS_THAN_PRE = CAN_NUMBER_BE_LESS_THAN_PRE;
    }

    public void setId(int id) {
        Id_2 = id;
    }

    public int getMAIN_CODE() {
        return MAIN_CODE;
    }

    public void setMAIN_CODE(int MAIN_CODE) {
        this.MAIN_CODE = MAIN_CODE;
    }

    public int getPOSSIBLE_CODE() {
        return POSSIBLE_CODE;
    }

    public void setPOSSIBLE_CODE(int POSSIBLE_CODE) {
        this.POSSIBLE_CODE = POSSIBLE_CODE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getCLIENT_ORDER() {
        return CLIENT_ORDER;
    }

    public void setCLIENT_ORDER(int CLIENT_ORDER) {
        this.CLIENT_ORDER = CLIENT_ORDER;
    }

    public boolean isSHOULD_ENTER_NUMBER() {
        return SHOULD_ENTER_NUMBER;
    }

    public void setSHOULD_ENTER_NUMBER(boolean SHOULD_ENTER_NUMBER) {
        this.SHOULD_ENTER_NUMBER = SHOULD_ENTER_NUMBER;
    }

    public CounterStateValueKeyModel() {
    }

    public CounterStateValueKeyModel(CounterStateValueKeyViewModel counterStateValueKeyViewModel) {
        this.setCLIENT_ORDER(counterStateValueKeyViewModel.getClientOrder());
        this.setId(counterStateValueKeyViewModel.getId());
        this.setMAIN_CODE(counterStateValueKeyViewModel.getMainCode());
        this.setPOSSIBLE_CODE(counterStateValueKeyViewModel.getPossibleCode());
        this.setTITLE(counterStateValueKeyViewModel.getTitle());
        this.setCAN_ENTER_NUMBER(counterStateValueKeyViewModel.getCanEnterNumber());
        this.setCAN_NUMBER_BE_LESS_THAN_PRE(counterStateValueKeyViewModel.getCanNumberBeLessThanPre());
        this.setIS_MANE(counterStateValueKeyViewModel.getIsMane());
        this.setIS_TAVIZI(counterStateValueKeyViewModel.IsTavizi());
        this.setSHOULD_ENTER_NUMBER(counterStateValueKeyViewModel.getShouldEnterNumber());
    }
}
