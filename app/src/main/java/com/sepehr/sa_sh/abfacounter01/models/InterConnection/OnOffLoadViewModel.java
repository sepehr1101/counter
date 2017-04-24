package com.sepehr.sa_sh.abfacounter01.models.InterConnection;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.util.UUID;

/**
 * Created by saeid on 3/30/2017.
 */
public class OnOffLoadViewModel {
    public UUID Id;
    public OnLoad OnLoad;
    public OffLoad OffLoad;

    public OnOffLoadViewModel(OnLoad onLoad, OffLoad offLoad) {
        this.OnLoad = onLoad;
        this.OffLoad = offLoad;
    }

    public OnOffLoadViewModel(OnOffLoadModel onOffLoadModel) {
        this.Id= UUID.fromString(onOffLoadModel.getS_id());
        this.OnLoad=new OnLoad(onOffLoadModel);
        this.OffLoad=new OffLoad(onOffLoadModel);
    }
}
