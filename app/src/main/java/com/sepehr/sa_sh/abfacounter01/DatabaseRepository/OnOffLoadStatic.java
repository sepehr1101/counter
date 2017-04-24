package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by saeid on 3/31/2017.
 */
public class OnOffLoadStatic {
    private OnOffLoadStatic() {
    }
    public static OnOffLoadModel GetOnOfLoad(String billId){
        final OnOffLoadModel onOffLoadModel=OnOffLoadModel
                .find(OnOffLoadModel.class, "BILL_ID= ? ", billId).get(0);
        return onOffLoadModel;
    }
}
