package com.sepehr.sa_sh.abfacounter01.Logic;

import com.sepehr.sa_sh.abfacounter01.AverageState;
import com.sepehr.sa_sh.abfacounter01.DateAndTime;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.HighLowModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by sa-sh on 8/9/2016.
 */
public class CounterNumberHelper implements ICounterNumberHelper{
    public CounterNumberHelper() {
    }
    //
    private double calculateAverage(OnOffLoadModel onOffload, KarbariModel karbari, int todayNumber){
        double average=0;
        int masraf=0;
        if(todayNumber>onOffload.getPreNumber()){
            masraf=todayNumber-onOffload.getPreNumber();
        }
        int dateDifference=DateAndTime.dateDifference(onOffload.getPreDate());
        dateDifference=dateDifference==0?1:dateDifference;
        double preAverage=((double)masraf/dateDifference)*30;
        boolean isKarbariMaskooni=karbari.IS_MASKOONI();
        if(isKarbariMaskooni && onOffload.getTedadMaskooni()>0){
            average=preAverage/onOffload.getTedadMaskooni();
        }
        else if(!isKarbariMaskooni && onOffload.getTedadKol()>0){
            average=preAverage/onOffload.getTedadKol();
        }
        return  average;
    }
    //
    public  AverageState getAverageState(OnOffLoadModel onOffload , KarbariModel karbari,
                                         HighLowModel highLowModel, int todayNumber){
        if(todayNumber==onOffload.preNumber){
            return AverageState.MASRAF_SEFR;
        }
        double todayRate=calculateAverage(onOffload,karbari,todayNumber);
        double preRate=onOffload.getPreAverage();
        if(karbari.IS_MASKOONI()){
            double maxAllowdRate=preRate+preRate*highLowModel.getHIGH_PERCENT_BOUND_MASKOONI()/100;
            double minAllowdRate=preRate-preRate*highLowModel.getLOW_PERCENT_BOUND_MASKOONI()/100;
            if(todayRate>maxAllowdRate){
                return AverageState.HIGH;
            }
            if(todayRate<minAllowdRate){
                return AverageState.LOW;
            }
        }
        else {
            double maxAllowdRate=preRate+preRate*highLowModel.getHIGH_PERCENT_RATE_BOUND_NON_MASKOONI()/100;
            double minAllowdRate=preRate-preRate*highLowModel.getLOW_PERCENT_RATE_BOUND_NON_MASKOONI()/100;
            if(todayRate>maxAllowdRate){
                return AverageState.HIGH;
            }
            if(todayRate<minAllowdRate){
                return AverageState.LOW;
            }
        }
        return AverageState.NORMAL;
    }

    public  static boolean isRestarted(int preNumber,int todayNumber ){
        if(preNumber>99900 && todayNumber<1000){
            return true;
        }
        return  false;
    }
}
