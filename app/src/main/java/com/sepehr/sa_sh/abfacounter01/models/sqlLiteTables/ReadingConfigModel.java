package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.ReadingConfigViewModel;

import java.math.BigDecimal;

/**
 * Created by saeid on 2/18/2017.
 */
public class ReadingConfigModel extends SugarRecord {
    int alalPercent;
    int imagePercent;
    int zoneId;
    String zoneTitle;
    BigDecimal trackNumber;
    String listNumber;
    boolean hasPreNumber;
    String fullName;
    int _index;
    boolean isActive;
    boolean isOnQeraatCode;

    public int get_index() {
        return _index;
    }

    public void set_index(int _index) {
        this._index = _index;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ReadingConfigModel() {
    }

    public ReadingConfigModel(ReadingConfigViewModel readingConfigViewModel,int i ,boolean isActive) {
        this.alalPercent=readingConfigViewModel.getAlalPercent();
        this.fullName=readingConfigViewModel.getFullName();
        this.hasPreNumber=readingConfigViewModel.isPreNumber();
        this.imagePercent=readingConfigViewModel.getImagePercent();
        this.listNumber=readingConfigViewModel.getListNumber();
        this.trackNumber=readingConfigViewModel.getTrackNumber();
        this.zoneId=readingConfigViewModel.getZoneId();
        this.zoneTitle=readingConfigViewModel.getZoneTitle();
        this._index=i;
        this.setIsActive(isActive);
        this.setIsOnQeraatCode(readingConfigViewModel.isOnQeraatCode());
    }

    public int getAlalPercent() {
        return alalPercent;
    }

    public int getImagePercent() {
        return imagePercent;
    }

    public int getZoneId() {
        return zoneId;
    }

    public String getZoneTitle() {
        return zoneTitle;
    }

    public BigDecimal getTrackNumber() {
        return trackNumber;
    }

    public String getListNumber() {
        return listNumber;
    }

    public boolean isPreNumber() {
        return hasPreNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isOnQeraatCode() {
        return isOnQeraatCode;
    }

    public void setIsOnQeraatCode(boolean isOnQeraatCode) {
        this.isOnQeraatCode = isOnQeraatCode;
    }
}
