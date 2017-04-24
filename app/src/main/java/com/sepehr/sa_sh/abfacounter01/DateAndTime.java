package com.sepehr.sa_sh.abfacounter01;

/*
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
*/

import java.util.Calendar;

/**
 * Created by sa-sh on 7/30/2016.
 */
public final class DateAndTime {
    private DateAndTime() {
    }

    public static int dateDifference(String preDate){
        Calendar oldDateCalendar=Calendar.getInstance();
        Calendar newDateCalendar=Calendar.getInstance();
        preDate=preDate.trim();
        int year=Integer.valueOf(preDate.substring(0,2))+1300;
        int month=Integer.valueOf(preDate.substring(2,4))-1;
        int day=Integer.valueOf(preDate.substring(4,6));
        JalaliCalendar jalaliCalendar=new JalaliCalendar();
        JalaliCalendar.YearMonthDate yearMonthDate= jalaliCalendar.jalaliToGregorian(new
                JalaliCalendar.YearMonthDate(year, month, day));
        oldDateCalendar.set(yearMonthDate.getYear(), yearMonthDate.getMonth(), yearMonthDate.getDate());
        int dateDifference=dateDifference(oldDateCalendar,newDateCalendar);
        return  dateDifference-1;//'az' nabayad hesab shavad
    }

    private static int dateDifference(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        int daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
    //
    public static String getPersianTodayDate(){
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        int year, month, day;
        year = jalaliCalendar.get(JalaliCalendar.YEAR);
        month = jalaliCalendar.get(JalaliCalendar.MONTH) + 1;
        day = jalaliCalendar.get(JalaliCalendar.DATE);

        String persianDate = year + "/" + month + "/" + day;
        return persianDate;
    }
    //
    public static String getPersianDbFormattedDate(){
        String persianDate_well_formated="";
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        int year, month, day;
        year = jalaliCalendar.get(JalaliCalendar.YEAR);
        month = jalaliCalendar.get(JalaliCalendar.MONTH) + 1;
        day = jalaliCalendar.get(JalaliCalendar.DATE);

        String paddedMonth = String.format("%2s", month).replace(' ', '0');
        String paddedDay = String.format("%2s", day).replace(' ', '0');
        persianDate_well_formated = (year % 100) + paddedMonth + paddedDay;
        return persianDate_well_formated;
    }
}
