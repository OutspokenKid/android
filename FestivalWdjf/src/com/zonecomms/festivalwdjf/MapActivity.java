package com.zonecomms.festivalwdjf;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.outspoken_kid.utils.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapActivity extends FragmentActivity {

	private GoogleMap map;
	private double latitude;
	private double longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.log("MapActivity.onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		LogUtils.log("MapActivity.onResume");
		
		if(map == null) {
			try {
				Intent intent = getIntent();
				latitude = intent.getDoubleExtra("latitude", 0);
				longitude = intent.getDoubleExtra("longitude", 0);
				
				map = ((SupportMapFragment)getSupportFragmentManager().
						findFragmentById(R.id.mapActivity_map)).getMap();
//				map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
				map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}