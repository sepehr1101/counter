package com.sepehr.sa_sh.abfacounter01.models.InterConnection;

import java.math.BigDecimal;

/**
 * Created by saeid on 2/18/2017.
 */
public class ReadingConfigViewModel {
    int alalPercent;
    int imagePercent;
    int zoneId;
    String zoneTitle;
    BigDecimal trackNumber;
    String listNumber;
    boolean hasPreNumber;
    String fullName;
    boolean isOnQeraatCode;

    public int getAlalPercent() {
        return alalPercent;
    }

    public void setAlalPercent(int alalPercent) {
        this.alalPercent = alalPercent;
    }

    public int getImagePercent() {
        return imagePercent;
    }

    public void setImagePercent(int imagePercent) {
        this.imagePercent = imagePercent;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneTitle() {
        return zoneTitle;
    }

    public void setZoneTitle(String zoneTitle) {
        this.zoneTitle = zoneTitle;
    }

    public BigDecimal getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(BigDecimal trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public boolean isPreNumber() {
        return hasPreNumber;
    }

    public void setHasPreNumber(boolean hasPreNumber) {
        this.hasPreNumber = hasPreNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isOnQeraatCode() {
        return isOnQeraatCode;
    }

    public void setIsOnQeraatCode(boolean isOnQeraatCode) {
        this.isOnQeraatCode = isOnQeraatCode;
    }

}
