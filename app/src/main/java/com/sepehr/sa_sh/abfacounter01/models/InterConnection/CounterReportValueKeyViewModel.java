package com.sepehr.sa_sh.abfacounter01.models.InterConnection;

/**
 * Created by saeid on 3/10/2017.
 */
public class CounterReportValueKeyViewModel {
    int Id;
    int Code;
    String Title;
    boolean IsAhad;
    boolean IsKarbari;
    boolean CanNumberBeLessThanPre;
    boolean IsTavizi;
    boolean IsActive;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isAhad() {
        return IsAhad;
    }

    public void setIsAhad(boolean isAhad) {
        IsAhad = isAhad;
    }

    public boolean isKarbari() {
        return IsKarbari;
    }

    public void setIsKarbari(boolean isKarbari) {
        IsKarbari = isKarbari;
    }

    public boolean isCanNumberBeLessThanPre() {
        return CanNumberBeLessThanPre;
    }

    public void setCanNumberBeLessThanPre(boolean canNumberBeLessThanPre) {
        CanNumberBeLessThanPre = canNumberBeLessThanPre;
    }

    public boolean isTavizi() {
        return IsTavizi;
    }

    public void setIsTavizi(boolean isTavizi) {
        IsTavizi = isTavizi;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }
}
