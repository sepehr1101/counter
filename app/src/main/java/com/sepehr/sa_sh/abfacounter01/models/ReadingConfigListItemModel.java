package com.sepehr.sa_sh.abfacounter01.models;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

/**
 * Created by saeid on 3/31/2017.
 */
public class ReadingConfigListItemModel {
    int itemId ;
    String title;
    boolean isChecked;

    public ReadingConfigListItemModel() {
    }

    public ReadingConfigListItemModel(int itemId, String title, boolean isChecked) {
        this.itemId = itemId;
        this.title = title;
        this.isChecked = isChecked;
    }

    public ReadingConfigListItemModel(ReadingConfigModel readingConfig){
        this.setItemId(readingConfig.get_index());
        this.setIsChecked(readingConfig.isActive());
        this.setTitle(readingConfig.getTrackNumber().toBigInteger().toString());
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
