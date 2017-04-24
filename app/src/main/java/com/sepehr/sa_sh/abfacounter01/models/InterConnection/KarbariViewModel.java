package com.sepehr.sa_sh.abfacounter01.models.InterConnection;

/**
 * Created by saeid on 3/13/2017.
 */
public class KarbariViewModel {
    int Id;
    String Title;
    boolean IsActive;
    int Group2;
    boolean IsMaskooni;
    boolean IsSakhtoSaz;
    boolean HasVibrate;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public int getGroup2() {
        return Group2;
    }

    public void setGroup2(int group2) {
        Group2 = group2;
    }

    public boolean isMaskooni() {
        return IsMaskooni;
    }

    public void setIsMaskooni(boolean isMaskooni) {
        IsMaskooni = isMaskooni;
    }

    public boolean isSakhtoSaz() {
        return IsSakhtoSaz;
    }

    public void setIsSakhtoSaz(boolean isSakhtoSaz) {
        IsSakhtoSaz = isSakhtoSaz;
    }

    public boolean isHasVibrate() {
        return HasVibrate;
    }

    public void setHasVibrate(boolean hasVibrate) {
        HasVibrate = hasVibrate;
    }
}
