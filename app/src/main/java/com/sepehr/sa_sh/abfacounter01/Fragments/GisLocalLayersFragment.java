package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.sepehr.sa_sh.abfacounter01.Adopters.SpinnerGisAdapter;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.models.SpinnerDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by saeid on 8/26/2017.
 */

public class GisLocalLayersFragment extends Fragment {
    private Spinner mBasemapSpinner,mNavigationSpinner;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;
    private ArcGISMap map;
    //private Basemap openStreetBasemap;
    private Basemap tswBoundaryBasemap;
    private LayerList mOperationalLayers;
    private ArcGISTiledLayer tswBoundaryTiledLayer;
    private Layer manholeLayer,sewerPipeLayer,networkPipeLayer, commonBlockLayer
            ,streetLayer,parcelLayer;
    private FeatureLayer counterLayer;
    private MenuItem manholeMenu, sewagePipeMenu,networkPipeMenu, eshterakBlockMenu,
            streetMenu,parcelMenu, counterMenu;
    private ProgressBar progressBarMapLoading;
    private ServiceFeatureTable counterFeatureTable;
    private ArcGISFeature mIdentifiedFeature;
    private SearchView searchView;
    private boolean mFeatureSelected = false;
    private final double SCALE=700;

    public GisLocalLayersFragment() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8277465837,none,8SH93PJPXMH2NERL1236");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  throws RuntimeException {
        View rootView = inflater.inflate(R.layout.fragment_gis_local_layers, container, false);
        initializeMap(rootView);
        setHasOptionsMenu(true);
        initializeChangeBaseMapSpinner(rootView);
        mLocationDisplay = mMapView.getLocationDisplay();
        if (mLocationDisplay == null) {
            Log.e("loc display", " is null");
        }

        initializeNavigationItems(rootView);
        return rootView;
    }



    @Override
    public void onPause(){
        super.onPause();
        mMapView.pause();
    }
    @Override
    public void onResume(){
        super.onResume();
        mMapView.resume();
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        initializeMenuItems(menu);
        setMenusChecked();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        handleMenuItemSelected(itemId);
        return super.onOptionsItemSelected(item);
    }

    protected void initializeMap(View rootView){
        initializeViewElements(rootView);

        tswBoundaryTiledLayer = new ArcGISTiledLayer(getString(R.string.tsw_boundary));
        tswBoundaryBasemap = new Basemap(tswBoundaryTiledLayer);
        map = new ArcGISMap(tswBoundaryBasemap);
        //ArcGISMap map = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 48.354406, -99.998267, 2);

        initializeServiceFeatureTable();
        initializeSubLayers();

        mOperationalLayers = map.getOperationalLayers();
        addSubLayers();
        // set the map to be displayed in this view
        mMapView.setMap(map);
        mapDrawingStateChangeListener();
        // enable magnifier
        mMapView.setMagnifierEnabled(true);
        // allow magnifier to pan near the edge of the map bounds
        mMapView.setCanMagnifierPanMap(true);
        mapOnTouchListener();
    }

    private void initializeViewElements(View rootView){
        mMapView = (MapView) rootView.findViewById(R.id.mapViewLayout);
        progressBarMapLoading = (ProgressBar) rootView.findViewById(R.id.progressBarMapLoading);
        searchView = (SearchView) rootView.findViewById(R.id.mapSearchView);
    }

    private void initializeServiceFeatureTable(){
        counterFeatureTable = new ServiceFeatureTable(getString(R.string.counter_feature_service));
        counterFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
    }

    private void initializeSubLayers() {
        manholeLayer = new ArcGISTiledLayer(getString(R.string.sewage_manhole));
        sewerPipeLayer = new ArcGISTiledLayer(getString(R.string.sewage_pipe));
        networkPipeLayer = new ArcGISTiledLayer(getString(R.string.network_pipe));
        commonBlockLayer = new ArcGISTiledLayer(getString(R.string.eshterak_block));
        streetLayer = new ArcGISTiledLayer(getString(R.string.street));
        parcelLayer = new ArcGISTiledLayer(getString(R.string.parcel));

        counterLayer=new FeatureLayer(counterFeatureTable);
        counterLayer.setSelectionColor(Color.CYAN);
        counterLayer.setSelectionWidth(6);
    }

    private void addSubLayers(){
        //mOperationalLayers.add(manholeLayer);
        //mOperationalLayers.add(sewerPipeLayer);
        //mOperationalLayers.add(networkPipeLayer);
        //mOperationalLayers.add(commonBlockLayer);
        //mOperationalLayers.add(streetLayer);
        mOperationalLayers.add(parcelLayer);
        mOperationalLayers.add(counterLayer);//todo added temp
    }

    private void initializeMenuItems(Menu menu) {
        manholeMenu = menu.getItem(0);
        sewagePipeMenu = menu.getItem(1);
        networkPipeMenu = menu.getItem(2);
        eshterakBlockMenu = menu.getItem(3);
        streetMenu = menu.getItem(4);
        parcelMenu = menu.getItem(5);
        counterMenu = menu.getItem(6);
    }

    private void setMenusChecked(){
        manholeMenu.setChecked(false);
        sewagePipeMenu.setChecked(false);
        networkPipeMenu.setChecked(false);
        eshterakBlockMenu.setChecked(true);
        streetMenu.setChecked(false);
        parcelMenu.setChecked(true);
        counterMenu.setChecked(true);//todo added temp
    }

    private void handleMenuItemSelected(int menuItemId){
        switch (menuItemId) {
            case R.id.manhole_menu:
                selectOrDeselectMenuItem(manholeMenu, manholeLayer);
                break;
            case R.id.sewer_pipe_menu:
                selectOrDeselectMenuItem(sewagePipeMenu,sewerPipeLayer);
                break;
            case R.id.network_pipe_menu:
                selectOrDeselectMenuItem(networkPipeMenu,networkPipeLayer);
                break;
            case R.id.eshterak_block_menu:
                selectOrDeselectMenuItem( eshterakBlockMenu,commonBlockLayer);
                break;
            case R.id.street_menu:
                selectOrDeselectMenuItem(streetMenu,streetLayer);
                break;
            case R.id.parcel_menu:
                selectOrDeselectMenuItem(parcelMenu,parcelLayer);
                break;
            case R.id.counter_menu:
                selectOrDeselectMenuItem(counterMenu,counterLayer);
                break;
            default:
                throw new RuntimeException("no menu find!");
        }
    }

    private void selectMenuItem(MenuItem menuItem,Layer subLayer){
        map.getOperationalLayers().add(subLayer);
        menuItem.setChecked(true);
    }
    private void unSelectMenuItem(MenuItem menuItem,Layer subLayer){
        map.getOperationalLayers().remove(subLayer);
        menuItem.setChecked(false);
    }
    private void selectOrDeselectMenuItem(MenuItem menuItem,Layer subLayer){
        if(menuItem.isChecked()){
            unSelectMenuItem(menuItem,subLayer);
        }else {
            selectMenuItem(menuItem,subLayer);
        }
    }

    private void mapDrawingStateChangeListener(){
        mMapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
            @Override
            public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
                if(drawStatusChangedEvent.getDrawStatus() == DrawStatus.IN_PROGRESS){
                    progressBarMapLoading.setVisibility(View.VISIBLE);
                }else if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED){
                    progressBarMapLoading.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void mapOnTouchListener(){
        try {
            mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getContext(), mMapView) {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    //updateFeture(e);
                    //addCounterFeauter(e);
                    return super.onSingleTapConfirmed(e);
                }

            });
        }catch (Exception e){
            Toast.makeText(getContext(),"بنظر میرسد این فیچر از به روز رسانی پشتیبانی نمیکند",Toast.LENGTH_SHORT);
        }

    }
    private void applyEditsToServer() {
        final ListenableFuture<List<FeatureEditResult>> applyEditsFuture =
                ((ServiceFeatureTable) ((FeatureLayer)counterLayer).getFeatureTable()).applyEditsAsync();
        applyEditsFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // get results of edit
                    List<FeatureEditResult> featureEditResultsList = applyEditsFuture.get();
                    if (!featureEditResultsList.get(0).hasCompletedWithErrors()) {
                        Toast.makeText(getContext(), "تغییرات مورد نظر شما اعمال شد. ObjectID: " + featureEditResultsList.get(0).getObjectId(), Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(getResources().getString(R.string.app_name), "به روز رسانی ناموفق: " + e.getMessage());
                }
            }
        });
    }
    private void initializeChangeBaseMapSpinner(View rootView){
        mBasemapSpinner = (Spinner) rootView.findViewById(R.id.baseMapSpinner);
        addBasemapGisSpinner();
    }
    private void addBasemapGisSpinner(){
        ArrayList<SpinnerDataModel> list = new ArrayList<>();
        list.add(new SpinnerDataModel(getString(R.string.local_gis_basemap), R.drawable.locationdisplaydisabled));//// TODO: change image
        list.add(new SpinnerDataModel(getString(R.string.osm) , R.drawable.locationdisplayon));//// TODO: change image 

        SpinnerGisAdapter adapter = new SpinnerGisAdapter(getActivity(), R.layout.gis_pan_mode_spinner_layout, R.id.txt, list);
        mBasemapSpinner.setAdapter(adapter);
        mBasemapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        map.setBasemap(tswBoundaryBasemap);
                        break;
                    case 1:
                        map.setBasemap(Basemap.createOpenStreetMap());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
    private void initializeNavigationItems(View rootView){
        mNavigationSpinner=(Spinner) rootView.findViewById(R.id.mapNavigationSpinner);
        addNavigationSpinner();
        setNavigationSpinnerClickListener();
        locationChangeStateListener();
    }
    private void addNavigationSpinner(){
        ArrayList<SpinnerDataModel> list = new ArrayList<>();
        list.add(new SpinnerDataModel("خاموش", R.drawable.locationdisplaydisabled));
        list.add(new SpinnerDataModel("روشن", R.drawable.locationdisplayon));
        list.add(new SpinnerDataModel("Re-Center", R.drawable.locationdisplayrecenter));
        list.add(new SpinnerDataModel("ناوبری", R.drawable.locationdisplaynavigation));
        list.add(new SpinnerDataModel("قطب نما", R.drawable.locationdisplayheading));

        SpinnerGisAdapter adapter = new SpinnerGisAdapter(getActivity(), R.layout.gis_pan_mode_spinner_layout, R.id.txt, list);
        mNavigationSpinner.setAdapter(adapter);
    }
    private void setNavigationSpinnerClickListener(){
        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        if (mLocationDisplay.isStarted())
                            mLocationDisplay.stop();
                        break;
                    case 1:
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 2:
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 3:
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 4:
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void locationChangeStateListener(){
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted()){
                    return;
                }

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }
                // Report other unknown failure types to the user - for example, location services may not
                // be enabled on the device.
                String message = String.format("خطا در DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                        .getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                // Update UI to reflect that the location display did not actually start
                mNavigationSpinner.setSelection(0, true);
            }
        });

    }

    private  void updateFeturer(MotionEvent e){
        if (!mFeatureSelected) {
            android.graphics.Point screenCoordinate = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
            double tolerance = 20;
            //Identify Layers to find features
            final ListenableFuture<IdentifyLayerResult> identifyFuture = mMapView.identifyLayerAsync(counterLayer, screenCoordinate, tolerance, false, 1);
            identifyFuture.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        // call get on the future to get the result
                        IdentifyLayerResult layerResult = identifyFuture.get();
                        List<GeoElement> resultGeoElements = layerResult.getElements();

                        //Debug.waitForDebugger();
                        if (resultGeoElements.size() > 0) {
                            if (resultGeoElements.get(0) instanceof ArcGISFeature) {
                                mIdentifiedFeature = (ArcGISFeature) resultGeoElements.get(0);
                                //Select the identified feature
                                (counterLayer).selectFeature(mIdentifiedFeature);
                                mFeatureSelected = true;
                                Toast.makeText(getContext(), "فیچر انتخاب شد  ،برای اعمال تغییرات اطلاعات مکانی روی نقشه تپ کنید", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "هیچ فیچری انتخاب نشده ، برای انتخاب تپ کنید", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e(getResources().getString(R.string.app_name), "به روز رسانی ناموفق: " + e.getMessage());
                    }
                }
            });
        } else {
            Point movedPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
            final Point normalizedPoint = (Point) GeometryEngine.normalizeCentralMeridian(movedPoint);
            mIdentifiedFeature.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //Debug.waitForDebugger();
                    boolean canIEdit=mIdentifiedFeature.canEditAttachments();
                    boolean canUpdateGeometr=mIdentifiedFeature.canUpdateGeometry();
                    if(!canUpdateGeometr){
                        Toast.makeText(getContext(),"این فیچر قابلیت به روز رسانی ندارد",Toast.LENGTH_SHORT);
                        return;
                    }
                    else {
                        mIdentifiedFeature.setGeometry(normalizedPoint);
                        final ListenableFuture<Void> updateFuture = counterLayer.getFeatureTable().updateFeatureAsync(mIdentifiedFeature);
                        updateFuture.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // track the update
                                    updateFuture.get();
                                    // apply edits once the update has completed
                                    if (updateFuture.isDone()) {
                                        applyEditsToServer();
                                        counterLayer.clearSelection();
                                        mFeatureSelected = false;
                                    } else {
                                        Log.e(getResources().getString(R.string.app_name), "به روز رسانی ناموفق");
                                    }
                                } catch (InterruptedException | ExecutionException e) {
                                    Log.e(getResources().getString(R.string.app_name), "علت به روز رسانی ناموفق: " + e.getMessage());
                                }
                            }
                        });
                    }
                }
            });
            mIdentifiedFeature.loadAsync();
        }
    }

    private void addCounterFeauter(MotionEvent e){

        android.graphics.Point screenPoint = new android.graphics.Point((int)e.getX(), (int)e.getY());
        // convert this to a map point
        Point mapPoint = mMapView.screenToLocation(screenPoint);

        // check features can be added, based on edit capabilities
        // create the attributes for the feature
        java.util.Map<String, Object> attributes = new HashMap<>();
        attributes.put("Eshterak_Code", "12345"); // Coded Values: [1: Manatee] etc...
        attributes.put("Address", "address"); // Coded Values: [0: No] , [1: Yes]
        attributes.put("Usage_", "1234");
        attributes.put("Customer_Name", "12345678");
        //attributes.put("Status ", "");
        //attributes.put("City_Name ", "چهاردانگه");
        attributes.put("C1", 1);
        attributes.put("C2", 0);
        attributes.put("C3", 2);
        //attributes.put("C4", 1);
        //attributes.put("X", mapPoint.getX());
        //attributes.put("Y", mapPoint.getY());
        attributes.put("ESHTERAK_CODE_NEW", "12345678");

        // Create a new feature from the attributes and an existing point geometry, and then add the feature
        Feature addedFeature = counterFeatureTable.createFeature(attributes, mapPoint);
        final ListenableFuture<Void> addFeatureFuture = counterFeatureTable.addFeatureAsync(addedFeature);
        addFeatureFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // check the result of the future to find out if/when the addFeatureAsync call succeeded - exception will be
                    // thrown if the edit failed
                    addFeatureFuture.get();

                    // if using an ArcGISFeatureTable, call getAddedFeaturesCountAsync to check the total number of features
                    // that have been added since last sync

                    // if dealing with ServiceFeatureTable, apply edits after making updates; if editing locally, then edits can
                    // be synchronized at some point using the SyncGeodatabaseTask.
                    if (counterFeatureTable instanceof ServiceFeatureTable) {
                        ServiceFeatureTable serviceFeatureTable = (ServiceFeatureTable)counterFeatureTable;
                        // apply the edits
                        final ListenableFuture<List<FeatureEditResult>> applyEditsFuture = serviceFeatureTable.applyEditsAsync();
                        applyEditsFuture.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final List<FeatureEditResult> featureEditResults = applyEditsFuture.get();
                                    // if required, can check the edits applied in this operation
                                    Log.e("arc success",String.format("Number of edits: %d", featureEditResults.size()));
                                } catch (InterruptedException | ExecutionException e) {
                                    Debug.waitForDebugger();
                                    Log.e("error",e.getMessage());
                                }
                            }
                        });
                    }

                } catch (InterruptedException | ExecutionException e) {
                    // executionException may contain an ArcGISRuntimeException with edit error information.
                    if (e.getCause() instanceof ArcGISRuntimeException) {
                        //Debug.waitForDebugger();
                        ArcGISRuntimeException agsEx = (ArcGISRuntimeException)e.getCause();
                        Log.e("error",String.format("Add Feature Error %d\n=%s", agsEx.getErrorCode(), agsEx.getMessage()));
                        Log.e("add feature additional",((ArcGISRuntimeException) e.getCause()).getAdditionalMessage());
                    } else {
                        Debug.waitForDebugger();
                        Log.e("error",e.getMessage());
                    }
                }
            }
        });

    }
    private void addCounterFeauter(MotionEvent e,CharSequence eshterak, int d1,int d2,int l1,int l2){

        android.graphics.Point screenPoint = new android.graphics.Point((int)e.getX(), (int)e.getY());
        // convert this to a map point
        Point mapPoint = mMapView.screenToLocation(screenPoint);

        // check features can be added, based on edit capabilities
        // create the attributes for the feature
        java.util.Map<String, Object> attributes = new HashMap<>();
        attributes.put("Eshterak_Code", eshterak); // Coded Values: [1: Manatee] etc...
        attributes.put("Address", ""); // Coded Values: [0: No] , [1: Yes]
        attributes.put("Usage_", "");
        attributes.put("Customer_Name", "");
        //attributes.put("Status ", "");
        //attributes.put("City_Name ", "چهاردانگه");
        attributes.put("C1", d1);
        attributes.put("C2", d2);
        attributes.put("C3", l1);
        attributes.put("C4", l2);
        attributes.put("X", mapPoint.getX());
        attributes.put("Y", mapPoint.getY());
        attributes.put("ESHTERAK_CODE_NEW", eshterak);

        // Create a new feature from the attributes and an existing point geometry, and then add the feature
        Feature addedFeature = counterFeatureTable.createFeature(attributes, mapPoint);
        final ListenableFuture<Void> addFeatureFuture = counterFeatureTable.addFeatureAsync(addedFeature);
        addFeatureFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // check the result of the future to find out if/when the addFeatureAsync call succeeded - exception will be
                    // thrown if the edit failed
                    addFeatureFuture.get();

                    // if using an ArcGISFeatureTable, call getAddedFeaturesCountAsync to check the total number of features
                    // that have been added since last sync

                    // if dealing with ServiceFeatureTable, apply edits after making updates; if editing locally, then edits can
                    // be synchronized at some point using the SyncGeodatabaseTask.
                    if (counterFeatureTable instanceof ServiceFeatureTable) {
                        ServiceFeatureTable serviceFeatureTable = (ServiceFeatureTable)counterFeatureTable;
                        // apply the edits
                        final ListenableFuture<List<FeatureEditResult>> applyEditsFuture = serviceFeatureTable.applyEditsAsync();
                        applyEditsFuture.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final List<FeatureEditResult> featureEditResults = applyEditsFuture.get();
                                    // if required, can check the edits applied in this operation
                                    Log.e("arc success",String.format("Number of edits: %d", featureEditResults.size()));
                                } catch (InterruptedException | ExecutionException e) {
                                    Debug.waitForDebugger();
                                    Log.e("error",e.getMessage());
                                }
                            }
                        });
                    }

                } catch (InterruptedException | ExecutionException e) {
                    // executionException may contain an ArcGISRuntimeException with edit error information.
                    if (e.getCause() instanceof ArcGISRuntimeException) {
                        //Debug.waitForDebugger();
                        ArcGISRuntimeException agsEx = (ArcGISRuntimeException)e.getCause();
                        Log.e("error",String.format("Add Feature Error %d\n=%s", agsEx.getErrorCode(), agsEx.getMessage()));
                        Log.e("add feature additional",((ArcGISRuntimeException) e.getCause()).getAdditionalMessage());
                    } else {
                        Debug.waitForDebugger();
                        Log.e("error",e.getMessage());
                    }
                }
            }
        });

    }

    protected void initializeSearchView(CharSequence eshterak) {
        searchView.setQueryHint(getString(R.string.search));
        searchView.setQuery(eshterak, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryAndSelectFeature(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void queryAndSelectFeature(final CharSequence eshterak){
        counterLayer.clearSelection();

        // create objects required to do a selection with a query
        QueryParameters query = new QueryParameters();
        //make search case insensitive
        query.setWhereClause("upper(Eshterak_Code) LIKE '%" + eshterak + "%'");//1203102450 for test

        final ListenableFuture<FeatureQueryResult> future = counterFeatureTable.queryFeaturesAsync(query);
        // add done loading listener to fire when the selection returns
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // call get on the future to get the result
                    FeatureQueryResult result = future.get();

                    // check there are some results
                    if (result.iterator().hasNext()) {

                        // get the extend of the first feature in the result to zoom to with the default scale
                        Feature feature = result.iterator().next();
                        Envelope envelope = feature.getGeometry().getExtent();
                        mMapView.setViewpointGeometryAsync(envelope, 200);
                        Geometry geometry= feature.getGeometry();
                        mMapView.setViewpointGeometryAsync(geometry);
                        mMapView.setViewpointScaleAsync(SCALE);
                        //Select the feature
                        counterLayer.selectFeature(feature);
                        mFeatureSelected=true;
                        mIdentifiedFeature = (ArcGISFeature)feature;
                    } else {
                        setAutopan();
                        Log.e("gis","eshterak not found");
                    }
                } catch (Exception e) {
                    Log.e(getResources().getString(R.string.app_name), "Feature search failed for: " + eshterak + ". Error=" + e.getMessage());
                }
            }
        });
    }

    private void setAutopan(){
        try {
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            if (!mLocationDisplay.isStarted()) {
                mLocationDisplay.startAsync();
            }
        }catch (Exception e){
            Log.e(getTag(),e.getMessage());
        }
    }
}

