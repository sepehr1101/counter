package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sepehr.sa_sh.abfacounter01.LatLang;

/**
 * Created by saeid on 3/1/2017.
 */
public class GeoTracker implements
        GoogleApiClient.ConnectionCallbacks ,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,IGeoTracker {
    private String TAG ;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public Location mLastLocation;
    // Google client to interact with Google API
    public GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;
    public LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 4; // 10 meters
    private int accuracy=0;
    private Context appContext;

    public GeoTracker(String TAG,Context appContext) {
        this.TAG = TAG;
        this.appContext=appContext;
    }
    //
    public LatLang getLatLang(){
        LatLang latLang;
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }catch (SecurityException e){
            e.printStackTrace();
            throw e;
        }

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            latLang=new LatLang(latitude,longitude);

        } else {
            latLang=new LatLang(0,0);
        }
        return  latLang;
    }
    //
    public void displayLocation() {
        try {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        }catch (SecurityException e){
            e.printStackTrace();
        }

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Log.e("loc:", "lat:" + latitude + "   lanng:" + longitude);
            //TODO save lat lang in db

        } else {
            Log.e("no loc","(دریافت مختصات میسر نشد ، لطفا اکتیو بودن GPS چک شود)");
            //TODO روشن کردن gps
        }
    }
    //
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(appContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    //
    public boolean checkPlayServices() {
        boolean resultBoolean=true;
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int resultCode=googleApiAvailability.isGooglePlayServicesAvailable(appContext);
     /*   int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(appContext);*/
        if (resultCode != ConnectionResult.SUCCESS) {
            resultBoolean=false;
        }
        return resultBoolean;
    }
    //
    public void start() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    //
    public void resume() {
        checkPlayServices();
        mRequestingLocationUpdates=true;
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
    //
    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    //
    public void pause() {
        stopLocationUpdates();
    }
    //
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }
    //
    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        Log.e("loc"," connected");
        displayLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    //
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        Log.e("loc"," suspended");
    }
    //
    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        if(location.hasAccuracy()){
            accuracy=Math.round(location.getAccuracy());
            Log.e("loc change,accuracy",accuracy+"");
        }
        // Displaying the new location on UI
        displayLocation();
    }
    //
    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 1 meters
    }
    //
    //
    /**
     * Method to toggle periodic location updates
     * */
    public void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            mRequestingLocationUpdates = false;
            // Stopping the location updates
            stopLocationUpdates();
            Log.d(TAG, "Periodic location updates stopped!");
        }
    }
    //
    /**
     * Starting the location updates
     * */
    public void startLocationUpdates() {
        try {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        }catch (SecurityException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Stopping location updates
     */
    public void stopLocationUpdates() {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }
    //
    public Location getLastLocation(){
        try {
            return LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }
        catch (SecurityException e){
            e.printStackTrace();
            throw e;
        }
    }
}
