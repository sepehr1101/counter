package com.sepehr.sa_sh.abfacounter01;

import com.orm.SugarRecord;

/**
 * Created by sa-sh on 8/8/2016.
 */
public class HighLow extends SugarRecord {
    int karbariCod;
    int highPercent;
    int lowPercent;

    public HighLow() {
    }
    //

    public HighLow(int highPercent, int lowPercent) {
        this.highPercent = highPercent;
        this.lowPercent = lowPercent;
    }
}
