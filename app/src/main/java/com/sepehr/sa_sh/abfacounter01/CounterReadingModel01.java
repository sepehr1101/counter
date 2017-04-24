package com.sepehr.sa_sh.abfacounter01;

import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.LoadModel;
import com.sepehr.sa_sh.abfacounter01.models.SpecialLoadModel;

import java.math.BigDecimal;

/**
 * Created by sa-sh on 7/23/2016.
 */
public class CounterReadingModel01 extends SugarRecord {
    //UUID Id;
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
    String possibleCounterSerial;
    Integer possibleQotrCode;
    String possibleEshterak;
    Integer possibleTedadMaskooni;
    Integer possibleTedadTejari;
    Integer possibleKarbariCode;
    Integer offLoadState;
    String listNumber;
    BigDecimal TrackNumber;
    Integer _position;
    Integer _reportHelper;
    Integer imageState;//hasImage =1 ,hasFilm=2,etc
    Integer counterRealState;//vaziate kontor dar system e billing (alan simafa)

    String COUNTR_SERIAL;
    Integer SIFOON_QOTR;
    boolean IS_COUNTER_NUMBER_SHOWN;
    public Integer Ahad_Masraf;
    Integer TEDAD_KHALI;
    //
    /* String karbari;
    Integer karbariCode;*/

    public CounterReadingModel01() {

    }

    //
    public CounterReadingModel01(CounterReadingEntity counterReadingEntity) {
        this.address = counterReadingEntity.address;
        this.aian = counterReadingEntity.aian;
        this.aian_mas = counterReadingEntity.aian_mas;
        this.aian_tej = counterReadingEntity.aian_tej;
        this.arse = counterReadingEntity.arse;
        this.average = counterReadingEntity.average;
        this.bed_bes = counterReadingEntity.bed_bes;
        this.BillID = counterReadingEntity.BillID;
        this.cod_enshab = counterReadingEntity.cod_enshab;
        this.CounterNumber = counterReadingEntity.CounterNumber;
        this.CounterReaderName = counterReadingEntity.CounterReaderName;
        this.CounterReaderUserCode = counterReadingEntity.CounterReaderUserCode;
        this.CounterStateCode = counterReadingEntity.CounterStateCode;
        this.enshab = counterReadingEntity.enshab;
        this.Eshterak = counterReadingEntity.Eshterak;
        this.family = counterReadingEntity.family;
        this.FromDate = counterReadingEntity.FromDate;
        this.FromEshterak = counterReadingEntity.FromEshterak;
        // this.Id=counterReadingEntity.Id;
        this.IsLocked = counterReadingEntity.IsLocked;
        this.masraf = counterReadingEntity.masraf;
        this.name = counterReadingEntity.name;
        this.MOBILE = counterReadingEntity.MOBILE;
        this.noe_va = counterReadingEntity.noe_va;
        this.pri_date = counterReadingEntity.pri_date;
        this.pri_no = counterReadingEntity.pri_no;
        this.PHONE_NO = counterReadingEntity.PHONE_NO;
        this.POST_COD = counterReadingEntity.POST_COD;
        this.Radif = counterReadingEntity.Radif;
        this.ted_khane = counterReadingEntity.ted_khane;
        this.tedad_mas = counterReadingEntity.tedad_mas;
        this.tedad_tej = counterReadingEntity.tedad_tej;
        this.tedad_vahd = counterReadingEntity.tedad_vahd;
        this.today_date = counterReadingEntity.today_date;
        this.today_no = counterReadingEntity.today_no;
        this.ToDate = counterReadingEntity.ToDate;
        this.ToEshterak = counterReadingEntity.ToEshterak;
        this.ZoneId = counterReadingEntity.ZoneId;
    }

    //
    public CounterReadingModel01(LoadModel loadModel) {
        this.BillID = loadModel.BillId;
        this.Eshterak = loadModel.Eshterak;
        this.name = loadModel.Name.trim();
        this.family = loadModel.Family.trim();
        this.address = loadModel.Address.trim();
        this.tedad_mas = new BigDecimal(loadModel.TedadMaskooni.toString());
        this.tedad_tej = new BigDecimal(loadModel.TedadNonMaskooni.toString());
        this.cod_enshab = new BigDecimal(loadModel.KarbariCod.toString());
        this.pri_no = loadModel.PreNumber.toString();
        this.average = loadModel.Average;
        this.pri_date = loadModel.PreDate;
        this.listNumber = loadModel.ListNumber;
        this.TrackNumber = loadModel.TrackNumber;
        this.enshab = loadModel.Qotr;
        this.Radif = loadModel.Radif;
        this.SIFOON_QOTR= loadModel.SifoonQotr;
        this.COUNTR_SERIAL= loadModel.CounterSerial;
        this.Ahad_Masraf=loadModel.AhadMasraf;
    }

    //
    public CounterReadingModel01(SpecialLoadModel specialLoadModel,
                                 int i,BigDecimal counterStateCode) {
        this.Radif = specialLoadModel.getRadif();
        this.counterRealState = specialLoadModel.getCounterStateCode().intValue();
        this._position = i;
        this.address = specialLoadModel.getAddress();
        this.average = specialLoadModel.getAverage();
        this.BillID = specialLoadModel.getBillId();
        this.cod_enshab = new BigDecimal(specialLoadModel.getKarbariCod().toString());
        this.enshab=specialLoadModel.getQotr();
        this.pri_no = specialLoadModel.getPreNumber().toString();
        this.CounterNumber = specialLoadModel.getCounterNumber();
        //this.CounterReaderUserCode=specialLoadModel.
        this.listNumber = specialLoadModel.getListNumber();
        this.TrackNumber = specialLoadModel.getTrackNumber();
        this.CounterStateCode=counterStateCode;
        this.name=specialLoadModel.getName();
        this.family=specialLoadModel.getFamily();
        this.Eshterak=specialLoadModel.getEshterak();
        this.tedad_mas=new BigDecimal(specialLoadModel.getTedadMaskooni());
        this.tedad_tej=new BigDecimal(specialLoadModel.getTedadNonMaskooni());
        this.pri_date=specialLoadModel.getPreDate();
        this.offLoadState=2;
    }
}
