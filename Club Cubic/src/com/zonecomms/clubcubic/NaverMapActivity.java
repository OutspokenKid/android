package com.zonecomms.clubcubic;

import android.os.Bundle;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.NMapPOIflagType;
import com.nhn.android.mapviewer.NMapViewerResourceProvider;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class NaverMapActivity extends NMapActivity {

	NMapView mMapView;
	NMapController mMapController;
	NMapViewerResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		// create map view
		mMapView = new NMapView(this);
	
		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(getString(R.string.naverMapKey));
	
		// set the activity content to the map view
		setContentView(mMapView);
	
		// initialize map view
		mMapView.setClickable(true);
	
		// use map controller to zoom in/out, pan and set map center, zoom level etc.
		mMapController = mMapView.getMapController();
		
		mMapView.setBuiltInZoomControls(true, null);
		
		// create resource provider
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
	
		// create overlay manager
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		
		int markerId = NMapPOIflagType.PIN;
	
		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
		poiData.addPOIitem(
				Double.parseDouble(getString(R.string.longitude)),
				Double.parseDouble(getString(R.string.latitude)),
				getString(R.string.app_name), markerId, 0);
		poiData.endPOIdata();
		
		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
		
		poiDataOverlay.showAllPOIdata(0);
	}
	
	public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
		if (errorInfo == null) { // success
			mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);
		} else { // fail
		}
	}	
}
