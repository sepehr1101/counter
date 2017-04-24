package com.sepehr.sa_sh.abfacounter01;

import com.orm.SugarRecord;

import java.math.BigDecimal;

/**
 * Created by saeid on 10/13/2016.
 */
public class QeireMojazModel extends SugarRecord {
    String preEshterak;
    String nextEshterak;
    String postalCode;
    BigDecimal Latitude;
    BigDecimal Longitude;
    Integer GisAccuracy;
    Integer tedadVahed;
    String imagePath;
    Integer offloadState;
    BigDecimal trackNumber;

    public QeireMojazModel() {
    }

    public QeireMojazModel(String preEshterak, String nextEshterak,
                           String postalCode, LatLang latLang,
                           Integer tedadVahed,String imagePath,
                           Integer offloadState,BigDecimal trackNumber) {
        this.preEshterak = preEshterak;
        this.nextEshterak = nextEshterak;
        this.postalCode = postalCode;
        this.Latitude = latLang.Latitude;
        this.Longitude = latLang.longtitude;
        this.tedadVahed=tedadVahed;
        this.imagePath=imagePath;
        this.offloadState=offloadState;
        this.trackNumber=trackNumber;
    }
}
