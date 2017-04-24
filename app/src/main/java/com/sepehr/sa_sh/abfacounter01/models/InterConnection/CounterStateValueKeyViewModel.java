package com.sepehr.sa_sh.abfacounter01.models.InterConnection;

/**
 * Created by saeid on 3/9/2017.
 */
public class CounterStateValueKeyViewModel {

    int Id;
    int MainCode;
    int PossibleCode;
    String Title;
    int ClientOrder;
    Boolean CanEnterNumber;
    Boolean IsMane;
    Boolean CanNumberBeLessThanPre;
    Boolean IsTavizi;
    Boolean ShouldEnterNumber;

    public Boolean IsTavizi() {
        return IsTavizi;
    }

    public void setIsTavizi(Boolean isTavizi) {
        IsTavizi = isTavizi;
    }

    public Boolean getCanEnterNumber() {
        return CanEnterNumber;
    }

    public void setCanEnterNumber(Boolean canEnterNumber) {
        CanEnterNumber = canEnterNumber;
    }

    public Boolean getIsMane() {
        return IsMane;
    }

    public void setIsMane(Boolean isMane) {
        IsMane = isMane;
    }

    public Boolean getCanNumberBeLessThanPre() {
        return CanNumberBeLessThanPre;
    }

    public void setCanNumberBeLessThanPre(Boolean canNumberBeLessThanPre) {
        CanNumberBeLessThanPre = canNumberBeLessThanPre;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMainCode() {
        return MainCode;
    }

    public void setMainCode(int mainCode) {
        MainCode = mainCode;
    }

    public int getPossibleCode() {
        return PossibleCode;
    }

    public void setPossibleCode(int possibleCode) {
        PossibleCode = possibleCode;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getClientOrder() {
        return ClientOrder;
    }

    public void setClientOrder(int clientOrder) {
        ClientOrder = clientOrder;
    }

    public Boolean getShouldEnterNumber() {
        return ShouldEnterNumber;
    }
}
