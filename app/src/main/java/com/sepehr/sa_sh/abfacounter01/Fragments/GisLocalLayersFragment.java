package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
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
import com.esri.arcgisruntime.mapping.view.MapView;
import com.sepehr.sa_sh.abfacounter01.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by saeid on 8/26/2017.
 */

public class GisLocalLayersFragment extends Fragment {

    private MapView mMapView;
    ArcGISMap map;
    private Basemap openStreetBasemap;
    private Basemap tswBoundaryBasemap;
    private LayerList mOperationalLayers;
    ArcGISTiledLayer tswBoundaryTiledLayer;
    Layer manholeLayer,sewerPipeLayer,networkPipeLayer, networkValveLayer,
            transmissionPipelineLayer,waterWellLayer,waterBranchLayer, commonBlockLayer,
            networkLineLayer,streetLayer,townLayer,parcelLayer,
            counterLayer;
    MenuItem manholeMenu,sewerPipeMenu,networkPipeMenu,networkValveMenu,
            transmissionPipelineMenu,waterWellMenu,waterBranchMenu, commonBlockMenu,
            networkLineMenu,streetMenu,townMenu,parcelMenu,
            counterMenu;
    ProgressBar progressBarMapLoading;
    ServiceFeatureTable manholeFeatureTable,sewerPipeFeatureTable,networkPipeFeatureTable, networkValveFeatureTable,
            transmissionPipelineFeatureTable,waterWellFeatureTable,waterBranchFeatureTable, commonBlockFeatureTable,
            networkLineFeatureTable,streetFeatureTable,townFeatureTable,parcelFeatureTable,
            counterFeatureTable;
    private ArcGISFeature mIdentifiedFeature;
    private boolean mFeatureSelected = false;

    public GisLocalLayersFragment() {
    }

    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gis_local_layers, container, false);
        initialize(rootView);
        setHasOptionsMenu(true);
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

    protected void initialize(View rootView){
        initializeViewElements(rootView);

        tswBoundaryTiledLayer = new ArcGISTiledLayer(getString(R.string.tsw_boundary));
        //ArcGISMapImageLayer tswAllLayersAsImage=new ArcGISMapImageLayer(getString(R.string.tsw_all_layers));
        tswBoundaryBasemap = new Basemap(tswBoundaryTiledLayer);
        map = new ArcGISMap(tswBoundaryBasemap);


        //ArcGISMap map = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 48.354406, -99.998267, 2);
        //tiledLayerBaseMap=new ArcGISTiledLayer(getString(R.string.tsw));

        initializeServiceFeatureTable();
        initializeSubLayers();
        //initializeFeatureLayers();

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
    }

    private void initializeServiceFeatureTable(){
        /*manholeFeatureTable= new ServiceFeatureTable(getString(R.string.manhole_feature_service));
        sewerPipeFeatureTable = new ServiceFeatureTable(getString(R.string.sewer_pipe_feature_service));
        networkPipeFeatureTable = new ServiceFeatureTable(getString(R.string.network_pipe_feature_service));
        networkValveFeatureTable = new ServiceFeatureTable(getString(R.string.network_valve_feature_service));
        transmissionPipelineFeatureTable = new ServiceFeatureTable(getString(R.string.transmission_pipeline_feature_service));
        waterWellFeatureTable = new ServiceFeatureTable(getString(R.string.water_well_feature_service));
        waterBranchFeatureTable = new ServiceFeatureTable(getString(R.string.water_branch_feature_service));
        commonBlockFeatureTable = new ServiceFeatureTable(getString(R.string.common_block_feature_service));
        networkLineFeatureTable = new ServiceFeatureTable(getString(R.string.network_line_feature_service));
        streetFeatureTable = new ServiceFeatureTable(getString(R.string.street_feature_service));
        townFeatureTable = new ServiceFeatureTable(getString(R.string.town_feature_service));
        parcelFeatureTable = new ServiceFeatureTable(getString(R.string.parcel_feature_service));*/
        counterFeatureTable = new ServiceFeatureTable(getString(R.string.counter_feature_service));
        counterFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
    }
    private void initializeFeatureLayers(){
       /* manholeLayer = new FeatureLayer(manholeFeatureTable);
        sewerPipeLayer = new FeatureLayer(sewerPipeFeatureTable);
        networkPipeLayer = new FeatureLayer(networkPipeFeatureTable);
        networkValveLayer = new FeatureLayer(networkValveFeatureTable);
        transmissionPipelineLayer = new FeatureLayer(transmissionPipelineFeatureTable);
        waterWellLayer = new FeatureLayer(waterWellFeatureTable);
        waterBranchLayer = new FeatureLayer(waterBranchFeatureTable);
        commonBlockLayer = new FeatureLayer(commonBlockFeatureTable);
        networkLineLayer = new FeatureLayer(networkLineFeatureTable);
        streetLayer = new FeatureLayer(streetFeatureTable);
        townLayer = new FeatureLayer(townFeatureTable);
        parcelLayer = new FeatureLayer(parcelFeatureTable);
        counterLayer = new FeatureLayer(counterFeatureTable);*/
    }
    private void initializeSubLayers() {
        manholeLayer = new ArcGISTiledLayer(getString(R.string.manhole));
        sewerPipeLayer = new ArcGISTiledLayer(getString(R.string.sewer_pipe));
        networkPipeLayer = new ArcGISTiledLayer(getString(R.string.network_pipe));
        networkValveLayer = new ArcGISTiledLayer(getString(R.string.network_valve));
        transmissionPipelineLayer = new ArcGISTiledLayer(getString(R.string.transmission_pipeline));
        waterWellLayer = new ArcGISTiledLayer(getString(R.string.water_well));
        waterBranchLayer = new ArcGISTiledLayer(getString(R.string.water_branch));
        commonBlockLayer = new ArcGISTiledLayer(getString(R.string.common_block));
        networkLineLayer = new ArcGISTiledLayer(getString(R.string.network_line));
        streetLayer = new ArcGISTiledLayer(getString(R.string.street));
        townLayer = new ArcGISTiledLayer(getString(R.string.town));
        parcelLayer = new ArcGISTiledLayer(getString(R.string.parcel));
        //counterLayer = new ArcGISTiledLayer(getString(R.string.counter));
        counterLayer=new FeatureLayer(counterFeatureTable);
        ((FeatureLayer)counterLayer).setSelectionColor(Color.CYAN);
        ((FeatureLayer)counterLayer).setSelectionWidth(6);
    }

    private void addSubLayers(){
        //mOperationalLayers.add(manholeLayer);
        //mOperationalLayers.add(sewerPipeLayer);
        //mOperationalLayers.add(networkPipeLayer);
        //mOperationalLayers.add(networkValveLayer);
        //mOperationalLayers.add(transmissionPipelineLayer);
        //mOperationalLayers.add(waterWellLayer);
        //mOperationalLayers.add(waterBranchLayer);
        mOperationalLayers.add(commonBlockLayer);
        //mOperationalLayers.add(networkLineLayer);
        mOperationalLayers.add(streetLayer);
        mOperationalLayers.add(townLayer);
        mOperationalLayers.add(parcelLayer);
        mOperationalLayers.add(counterLayer);
    }

    private void initializeMenuItems(Menu menu) {
        manholeMenu = menu.getItem(0);
        sewerPipeMenu = menu.getItem(1);
        networkPipeMenu = menu.getItem(2);
        networkValveMenu = menu.getItem(3);
        transmissionPipelineMenu = menu.getItem(4);
        waterWellMenu = menu.getItem(5);
        waterBranchMenu = menu.getItem(6);
        commonBlockMenu = menu.getItem(7);
        networkLineMenu = menu.getItem(8);
        streetMenu = menu.getItem(9);
        townMenu = menu.getItem(10);
        parcelMenu = menu.getItem(11);
        counterMenu = menu.getItem(12);
    }

    private void setMenusChecked(){
        manholeMenu.setChecked(true);
        sewerPipeMenu.setChecked(true);
        networkPipeMenu.setChecked(true);
        networkValveMenu.setChecked(true);
        transmissionPipelineMenu.setChecked(true);
        waterWellMenu.setChecked(true);
        waterBranchMenu.setChecked(true);
        commonBlockMenu.setChecked(true);
        networkLineMenu.setChecked(true);
        streetMenu.setChecked(true);
        townMenu.setChecked(true);
        parcelMenu.setChecked(true);
        counterMenu.setChecked(true);
    }

    private void handleMenuItemSelected(int menuItemId){
        switch (menuItemId) {
            case R.id.manhole_menu:
                selectOrDeselectMenuItem(manholeMenu, manholeLayer);
                break;
            case R.id.sewer_pipe_menu:
                selectOrDeselectMenuItem(sewerPipeMenu,sewerPipeLayer);
                break;
            case R.id.network_pipe_menu:
                selectOrDeselectMenuItem(networkPipeMenu,networkPipeLayer);
                break;
            case R.id.network_valve_menu:
                selectOrDeselectMenuItem(networkValveMenu,networkValveLayer);
                break;
            case R.id.transmission_pipeline_menu:
                selectOrDeselectMenuItem(transmissionPipelineMenu,transmissionPipelineLayer);
                break;
            case R.id.water_well_menu:
                selectOrDeselectMenuItem(waterWellMenu,waterWellLayer);
                break;
            case R.id.water_branch_menu:
                selectOrDeselectMenuItem(waterBranchMenu,waterBranchLayer);
                break;
            case R.id.common_block_menu:
                selectOrDeselectMenuItem(commonBlockMenu,commonBlockLayer);
                break;
            case R.id.network_line_menu:
                selectOrDeselectMenuItem(networkLineMenu,networkLineLayer);
                break;
            case R.id.street_menu:
                selectOrDeselectMenuItem(streetMenu,streetLayer);
                break;
            case R.id.town_menu:
                selectOrDeselectMenuItem(townMenu,townLayer);
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
                                            ((FeatureLayer) counterLayer).selectFeature(mIdentifiedFeature);
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
                                    final ListenableFuture<Void> updateFuture = ((FeatureLayer) counterLayer).getFeatureTable().updateFeatureAsync(mIdentifiedFeature);
                                    updateFuture.addDoneListener(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // track the update
                                                updateFuture.get();
                                                // apply edits once the update has completed
                                                if (updateFuture.isDone()) {
                                                    applyEditsToServer();
                                                    ((FeatureLayer) counterLayer).clearSelection();
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

}

