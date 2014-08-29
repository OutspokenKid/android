package com.outspoken_kid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public static final int TYPE_NONE = 0;
	public static final int TYPE_MOBILE = 1;
	public static final int TYPE_WIFI = 2;
	
	public static int checkNetworkStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo ni_wf = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if(ni.isConnected() || ni_wf.isConnected()) {
			return TYPE_WIFI;
		} else if(ni.isConnected() && !ni_wf.isConnected()) {
			return TYPE_MOBILE;
		} else {
			return TYPE_NONE;
		}
	}
}
