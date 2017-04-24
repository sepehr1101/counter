package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;

import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.KarbariViewModel;

/**
 * Created by saeid on 3/13/2017.
 */
public class KarbariModel extends SugarRecord {
    int CODE;
    String TITLE;
    boolean IS_ACTIVE;
    int GROUP_2;
    boolean IS_MASKOONI;
    boolean IS_SAKHOTO_SAZ;
    boolean HAS_VIBRATE;

    public KarbariModel() {
    }

    public KarbariModel(KarbariViewModel karbariViewModel) {
        this.CODE=karbariViewModel.getId();
        this.GROUP_2=karbariViewModel.getGroup2();
        this.HAS_VIBRATE=karbariViewModel.isHasVibrate();
        this.IS_ACTIVE=karbariViewModel.isActive();
        this.IS_MASKOONI=karbariViewModel.isMaskooni();
        this.IS_SAKHOTO_SAZ=karbariViewModel.isSakhtoSaz();
        this.TITLE=karbariViewModel.getTitle();
    }

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public boolean IS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(boolean IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public int getGROUP_2() {
        return GROUP_2;
    }

    public void setGROUP_2(int GROUP_2) {
        this.GROUP_2 = GROUP_2;
    }

    public boolean IS_MASKOONI() {
        return IS_MASKOONI;
    }

    public void setIS_MASKOONI(boolean IS_MASKOONI) {
        this.IS_MASKOONI = IS_MASKOONI;
    }

    public boolean IS_SAKHOTO_SAZ() {
        return IS_SAKHOTO_SAZ;
    }

    public void setIS_SAKHOTO_SAZ(boolean IS_SAKHOTO_SAZ) {
        this.IS_SAKHOTO_SAZ = IS_SAKHOTO_SAZ;
    }

    public boolean isHAS_VIBRATE() {
        return HAS_VIBRATE;
    }

    public void setHAS_VIBRATE(boolean HAS_VIBRATE) {
        this.HAS_VIBRATE = HAS_VIBRATE;
    }
}
