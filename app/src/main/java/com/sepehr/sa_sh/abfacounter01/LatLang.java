package com.sepehr.sa_sh.abfacounter01;

import java.math.BigDecimal;

/**
 * Created by saeid on 10/13/2016.
 */
public class LatLang {
    public BigDecimal Latitude;
    public BigDecimal longtitude;

    public LatLang() {
    }

    public LatLang(double latitude, double longtitude) {
        this.Latitude =new BigDecimal(latitude);
        this.longtitude =new BigDecimal(longtitude);
    }
}
