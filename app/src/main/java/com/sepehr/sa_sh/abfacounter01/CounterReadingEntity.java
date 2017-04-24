package com.sepehr.sa_sh.abfacounter01;

import java.math.BigDecimal;
import java.util.UUID;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sa-sh on 7/23/2016.
 */
public class CounterReadingEntity {
   // UUID Id;
    BigDecimal Radif;
    String BillID;
    String Eshterak;
    Integer GeneratedBy;
    /*Calendar GenerationDateTime;*/
    Integer CounterReaderUserCode;
    String CounterReaderName;
    String ZoneId;
    String FromEshterak;
    String ToEshterak;
    String FromDate;
    String ToDate;
    /* Calendar LoadDateTime;

    Calendar OffLoadDateTime;

    Calendar RegistrationDateTime;*/

    String RegistrationDateJalali;

    BigDecimal CounterNumber;
    BigDecimal CounterStateCode;
    BigDecimal Latitude;
    BigDecimal Longitude;
    String PossiblePhoneNumber;
    String PossibleMobile;
    String PossibleAddress;
    Integer GisAccuracy;
    boolean IsLocked;
    //
    String name;
    String family;
    BigDecimal enshab;
    BigDecimal cod_enshab;
    BigDecimal tedad_vahd;
    BigDecimal tedad_mas;
    BigDecimal ted_khane;
    BigDecimal tedad_tej;
    BigDecimal arse;
    BigDecimal aian;
    BigDecimal aian_mas;
    BigDecimal aian_tej;
    String address;
    BigDecimal bed_bes;
    BigDecimal noe_va;
    String POST_COD;
    String PHONE_NO;
    String MOBILE;
    //
    String pri_no;
    String today_no;
    String pri_date;
    String today_date;
    String masraf;
    BigDecimal average;
    //
    String karbari;
    Integer karbariCode;
}
