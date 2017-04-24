package com.sepehr.sa_sh.abfacounter01;

import com.orm.SugarRecord;

/**
 * Created by sa-sh on 8/8/2016.
 */
public class Karbari extends SugarRecord {
    int karbariCod;
    String karbariTitle;
    int karbariGroup_FK;

    public Karbari() {
    }

    public Karbari(int karbariCod, String karbariTitle, int karbariGroup_FK) {
        this.karbariCod = karbariCod;
        this.karbariTitle = karbariTitle;
        this.karbariGroup_FK = karbariGroup_FK;
    }
}
