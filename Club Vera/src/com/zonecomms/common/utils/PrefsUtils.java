package com.zonecomms.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * v1.0.0
 * 
 * @author HyungGunKim
 */
public class PrefsUtils {

	public static String getStringFromPrefsInOtherApp(Context context, String packageName, String prefsName, String key) {
		
		if(context == null) {
			return null;
		}
		
		try {
			Context otherContext = context.createPackageContext(packageName, 0);
			SharedPreferences prefs = otherContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			return prefs.getString(key, null);
		} catch(Exception e) {
		}
		
		return null;
	}
}
