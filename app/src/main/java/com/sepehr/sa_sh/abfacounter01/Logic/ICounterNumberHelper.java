package com.sepehr.sa_sh.abfacounter01.Logic;

import com.sepehr.sa_sh.abfacounter01.AverageState;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.HighLowModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by saeid on 4/30/2017.
 */

public interface ICounterNumberHelper {
    AverageState getAverageState(OnOffLoadModel onOffload , KarbariModel karbari,
                                 HighLowModel highLowModel, int todayNumber);
}
