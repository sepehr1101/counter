package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.location.Location;

import com.sepehr.sa_sh.abfacounter01.LatLang;

/**
 * Created by saeid on 3/1/2017.
 */
public interface IGeoTracker {
    LatLang getLatLang();
    void displayLocation();
    void buildGoogleApiClient();
    boolean checkPlayServices();
    void start();
    void resume();
    void stop();
    void pause();
    void createLocationRequest();
    void startLocationUpdates();
    void stopLocationUpdates();
    void togglePeriodicLocationUpdates();
    Location getLastLocation();
}
