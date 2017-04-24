package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;

/**
 * Created by sa-sh on 8/8/2016.
 */
public class KarbariGroup extends SugarRecord {
    int gropuId;
    String groupTitle;

    public KarbariGroup() {
    }

    public KarbariGroup(int gropuId, String groupTitle) {
        this.gropuId = gropuId;
        this.groupTitle = groupTitle;
    }

    public int getGropuId() {
        return gropuId;
    }

    public void setGropuId(int gropuId) {
        this.gropuId = gropuId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
