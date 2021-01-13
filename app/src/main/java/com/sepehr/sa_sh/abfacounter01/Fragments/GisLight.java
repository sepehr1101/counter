package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.esri.arcgisruntime.geometry.SpatialReferences;
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
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.InputFilterMinMax;
import com.sepehr.sa_sh.abfacounter01.models.SpinnerDataModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GisLight extends DialogFragment {
    private Spinner mBasemapSpinner;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;
    private ArcGISMap map;
    //private Basemap openStreetBasemap;
    private Basemap tswBoundaryBasemap;
    private LayerList mOperationalLayers;
    private ArcGISTiledLayer tswBoundaryTiledLayer;
    private Layer streetLayer,parcelLayer;
    private FeatureLayer counterLayer,parcelLayerGolestan;
    private ProgressBar progressBarMapLoading;
    private ServiceFeatureTable counterFeatureTable , parcelFeatureTableGolestan;
    private ArcGISFeature counterIdentifiedFeature , parcelIdentifiedFeatureGolestan;
    private SearchView searchView;
    LinearLayout confirmWrapper,addOrCancel;
    EditText d1,d2,l1,l2,r1,r2;
    Button cancel,ok,addCounter,cancelSelect;
    Point mapPoint=null;
    private boolean isCounterFeatureSelected = false,isGolestanParcelSelected;
    private final double SCALE=700;
    String eshterak;

    public GisLight() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8277465837,none,8SH93PJPXMH2NERL1236");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  throws RuntimeException {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        View rootView = inflater.inflate(R.layout.fragment_gis_light, container, false);
        initializeMap(rootView);
        setHasOptionsMenu(true);
        initializeChangeBaseMapSpinner(rootView);
        mLocationDisplay = mMapView.getLocationDisplay();
        if (mLocationDisplay == null) {
            Log.e("loc display", " is null");
        }
        setAutopan();
        return rootView;
    }

    @Override
    public int getTheme() {
        //return super.getTheme();
        return R.style.FullScreenDialog;
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

    protected void initializeMap(View rootView){
        initializeViewElements(rootView);

        tswBoundaryTiledLayer = new ArcGISTiledLayer(getString(R.string.tsw_boundary));
        tswBoundaryBasemap = new Basemap(tswBoundaryTiledLayer);
        map = new ArcGISMap(tswBoundaryBasemap);

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
        onOkClickListener();
        onCancelClickListener();
        //onCancelSelectListener();
    }

    private void initializeViewElements(View rootView){
        mMapView = (MapView) rootView.findViewById(R.id.mapViewLayout);
        progressBarMapLoading = (ProgressBar) rootView.findViewById(R.id.progressBarMapLoading);
        searchView = (SearchView) rootView.findViewById(R.id.mapSearchView);
        confirmWrapper = (LinearLayout) rootView.findViewById(R.id.confirmWrapper);
        d1=(EditText)rootView.findViewById(R.id.d1);
        d2=(EditText)rootView.findViewById(R.id.d2);
        l1=(EditText)rootView.findViewById(R.id.l1);
        l2=(EditText)rootView.findViewById(R.id.l2);
        r1=(EditText)rootView.findViewById(R.id.r1);
        r2=(EditText)rootView.findViewById(R.id.r2);
        cancel=(Button)rootView.findViewById(R.id.cancel);
        ok=(Button)rootView.findViewById(R.id.ok);

        addOrCancel=(LinearLayout)rootView.findViewById(R.id.addOrCancel);
        addCounter=(Button)rootView.findViewById(R.id.addCounter);
        cancelSelect=(Button)rootView.findViewById(R.id.cancelSelect);

        d1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "30")});
        d2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        l1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "30")});
        l2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        r1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        r2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "90")});
        eshterak=((DisplayViewPager)getActivity()).getEshterak();
        initializeSearchView(eshterak);
    }

    private void initializeServiceFeatureTable(){
        counterFeatureTable = new ServiceFeatureTable(getString(R.string.counter_feature_service));
        counterFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);

        //parcelFeatureTableGolestan=new ServiceFeatureTable(getString(R.string.parcel_feature_service_golestan));
        //parcelFeatureTableGolestan.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
    }

    private void initializeSubLayers() {
        streetLayer = new ArcGISTiledLayer(getString(R.string.street));
        parcelLayer = new ArcGISTiledLayer(getString(R.string.parcel));

        counterLayer=new FeatureLayer(counterFeatureTable);
        counterLayer.setSelectionColor(Color.CYAN);
        counterLayer.setSelectionWidth(6);

       /* parcelLayerGolestan =new FeatureLayer(parcelFeatureTableGolestan);
        parcelLayerGolestan.setSelectionColor(Color.BLUE);
        parcelLayerGolestan.setSelectionWidth(6);*/

    }

    private void addSubLayers(){
        mOperationalLayers.add(streetLayer);
        mOperationalLayers.add(parcelLayer);
        mOperationalLayers.add(counterLayer);
        //mOperationalLayers.add(parcelLayerGolestan);
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
                  /*  if(!isGolestanParcelSelected) {
                        selectGolestanParcel(e);
                    }
                    else {*/
                        fillPreAddParams(e);
//                    }
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    updateFeture(e);
                    super.onLongPress(e);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),"بنظر میرسد این فیچر از به روز رسانی پشتیبانی نمیکند",Toast.LENGTH_SHORT).show();
        }

    }
    private void applyEditsToServer() {
        final ListenableFuture<List<FeatureEditResult>> applyEditsFuture =
                ((ServiceFeatureTable) (counterLayer).getFeatureTable()).applyEditsAsync();
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
        list.add(new SpinnerDataModel(getString(R.string.local_gis_basemap), R.drawable.locationdisplaydisabled));
        list.add(new SpinnerDataModel(getString(R.string.osm) , R.drawable.locationdisplayon));

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

    private void updateFeture(MotionEvent e){
        if (!isCounterFeatureSelected) {
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
                                counterIdentifiedFeature = (ArcGISFeature) resultGeoElements.get(0);
                                //Select the identified feature
                                (counterLayer).selectFeature(counterIdentifiedFeature);
                                isCounterFeatureSelected = true;
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
            counterIdentifiedFeature.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //Debug.waitForDebugger();
                    boolean canIEdit=counterIdentifiedFeature.canEditAttachments();
                    boolean canUpdateGeometr=counterIdentifiedFeature.canUpdateGeometry();
                    if(!canUpdateGeometr){
                        Toast.makeText(getContext(),"این فیچر قابلیت به روز رسانی ندارد",Toast.LENGTH_SHORT);
                        return;
                    }
                    else {
                        counterIdentifiedFeature.setGeometry(normalizedPoint);
                        final ListenableFuture<Void> updateFuture = counterLayer.getFeatureTable().updateFeatureAsync(counterIdentifiedFeature);
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
                                        isCounterFeatureSelected = false;
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
            counterIdentifiedFeature.loadAsync();
        }
    }

    private void selectGolestanParcel(MotionEvent e){
        if (!isGolestanParcelSelected) {
            android.graphics.Point screenCoordinate = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
            double tolerance = 20;
            //Identify Layers to find features
            final ListenableFuture<IdentifyLayerResult> identifyFuture = mMapView.identifyLayerAsync(parcelLayerGolestan, screenCoordinate, tolerance, false, 1);
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
                                parcelIdentifiedFeatureGolestan = (ArcGISFeature) resultGeoElements.get(0);
                                //Select the identified feature
                                (parcelLayerGolestan).selectFeature(parcelIdentifiedFeatureGolestan);
                                isGolestanParcelSelected = true;
                                addOrCancel.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "هیچ فیچری انتخاب نشده ، برای انتخاب تپ کنید", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e(getResources().getString(R.string.app_name), "انتخاب عارضه ناموفق: " + e.getMessage());
                    }
                }
            });
        }
    }


    private void addCounterFeauter(MotionEvent e){
        if(isCounterFeatureSelected){
            return;
        }
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
                                    //Debug.waitForDebugger();
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
                        //Debug.waitForDebugger();
                        Log.e("error",e.getMessage());
                    }
                }
            }
        });

    }

    private void fillPreAddParams(MotionEvent e){
        if(confirmWrapper.getVisibility()==View.VISIBLE || isCounterFeatureSelected /*|| !isGolestanParcelSelected*/){
            return;
        }
        //addOrCancel.setVisibility(View.GONE);
        confirmWrapper.setVisibility(View.VISIBLE);
        android.graphics.Point screenPoint = new android.graphics.Point((int)e.getX(), (int)e.getY());
        // convert this to a map point
        mapPoint = mMapView.screenToLocation(screenPoint);
    }
    private Feature makeAddFeature(){
        Point wgs84Point=projectToWgs84(mapPoint);
        int _d1,_d2,_l1,_l2,_r1,_r2;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        _d1=Integer.parseInt(d1.getText().toString());
        _d2=Integer.parseInt(d2.getText().toString());
        _l1=Integer.parseInt(l1.getText().toString());
        _l2=Integer.parseInt(l2.getText().toString());
        _r1=Integer.parseInt(r1.getText().toString());
        _r2=Integer.parseInt(r2.getText().toString());
        // check features can be added, based on edit capabilities
        // create the attributes for the feature
        java.util.Map<String, Object> attributes = new HashMap<>();
        attributes.put("Eshterak_Code",eshterak);
        attributes.put("ESHTERAK_CODE_NEW", eshterak);
        attributes.put("City_Name ", "");
        attributes.put("C1", _d1);
        attributes.put("C2", _d2);
        attributes.put("C3", _l1);
        attributes.put("C4", _l2);
        attributes.put("C5", _r1);
        attributes.put("C6", _r2);
        attributes.put("User_Code", ((DisplayViewPager)getActivity()).getUserCode());
        attributes.put("Date_Time ",currentDateandTime );
        attributes.put("X", wgs84Point.getX());
        attributes.put("Y", wgs84Point.getY());
        attributes.put("Description","");

        // Create a new feature from the attributes and an existing point geometry, and then add the feature
        Feature addedFeature = counterFeatureTable.createFeature(attributes, mapPoint);
        return addedFeature;
    }

    private void addCounterFeauter(){
        if(isCounterFeatureSelected){
            return;
        }
        Feature addedFeature=makeAddFeature();
        if(addedFeature==null){
            return;
        }
        //Debug.waitForDebugger();
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
                        ServiceFeatureTable serviceFeatureTable = counterFeatureTable;
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
                                    //Debug.waitForDebugger();
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
                        //Debug.waitForDebugger();
                        Log.e("error",e.getMessage());
                    }
                }finally {
                    confirmWrapper.setVisibility(View.GONE);
                    clearEditTexts();
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
                        isCounterFeatureSelected =true;
                        counterIdentifiedFeature = (ArcGISFeature)feature;
                    } else {
                        setAutopan();
                        Toast.makeText(getContext(), "جستجوی اشتراک: " + eshterak + " میسر نشد",Toast.LENGTH_SHORT);
                        Log.e("gis","eshterak not found");
                    }
                } catch (Exception e) {
                    setAutopan();
                    Log.e(getResources().getString(R.string.app_name), "جستجوی اشتراک: " + eshterak + " میسر نشد"+ e.getCause());
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
    //
    private void onOkClickListener(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(d1.getText().toString().length()>0 && d2.getText().toString().length()>0 &&
                        l1.getText().toString().length()>0 && l2.getText().toString().length()>0){
                    addCounterFeauter();
                }else {
                    Toast.makeText(getContext(),"لطفا فاصله ها را با دقت بیشتری وارد فرمایید",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void onCancelClickListener(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapPoint=null;
                isCounterFeatureSelected =false;
                confirmWrapper.setVisibility(View.GONE);
                clearEditTexts();
            }
        });
    }
    private void onCancelSelectListener(){
        cancelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parcelLayerGolestan.clearSelection();
                parcelIdentifiedFeatureGolestan=null;
                isGolestanParcelSelected=false;
                addOrCancel.setVisibility(View.GONE);
            }
        });
    }

    private void clearEditTexts(){
        d1.setText("");
        d2.setText("");
        l1.setText("");
        l2.setText("");
        r1.setText("");
        r2.setText("");
    }
    private Point projectToWgs84(Point point){
        Point wgs84Point = (Point) GeometryEngine.project(point, SpatialReferences.getWgs84());
        return wgs84Point;
    }
}
