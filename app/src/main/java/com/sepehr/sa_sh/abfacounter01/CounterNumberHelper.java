package com.sepehr.sa_sh.abfacounter01;


import android.text.style.TtsSpan;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.http.PUT;

/**
 * Created by sa-sh on 8/9/2016.
 */
public final class CounterNumberHelper {
    private CounterNumberHelper() {
    }

    //
    public static boolean isCounterNumberValid(int preNumber,int todayNumber){
        boolean result=true;
        if(preNumber>todayNumber){
            result=false;
        }
        return  result;
    }
    //
    public static double CalculateAverage(int preNumber,int todayNumber,String preDate){
        double average;
        int masraf=todayNumber-preNumber;
        int dateDifference=DateAndTime.dateDifference(preDate);
        average=(double)masraf/dateDifference;
        return  average;
    }
    //
    public static AverageState DetermineAverageState(double preAverage,double todayAverage,
                        HighLow highLow,int tedadMaskooni){

        if(tedadMaskooni==0){
            return  AverageState.NORMAL;
        }
        double maxAllowedValue;
        double minAllowedValue;
        maxAllowedValue=preAverage+(highLow.highPercent*preAverage/100);
        minAllowedValue=preAverage-(highLow.lowPercent*preAverage/100);
        if(todayAverage==0){
            return AverageState.MASRAF_SEFR;
        }
        if(maxAllowedValue<todayAverage){
            return AverageState.HIGH;
        }
        if (todayAverage<minAllowedValue){
            return  AverageState.LOW;
        }

        return AverageState.NORMAL;
    }
    //
    public  static boolean isRestarted(int preNumber,int todayNumber ){
        if(preNumber>99900 && todayNumber<1000){
            return true;
        }
        return  false;
    }
}
