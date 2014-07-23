package com.outspoken_kid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * v1.0.0
 * 
 * @author HyungGunKim
 */
public class SharedPrefsUtils {
	
	private static Context context;
	
	public static void setContext(Context context) {
		
		if(SharedPrefsUtils.context == null) {
			SharedPrefsUtils.context = context;
		}
	}
	
	public static int getIntegerFromPrefs(String prefsName, String key) {
		if(context == null) {
			return 0;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			return prefs.getInt(key, 0);
		} catch(Exception e) {
			return 0;
		}
	}
	
	public static Long getLongFromPrefs(String prefsName, String key) {
		if(context == null) {
			return 0L;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			return prefs.getLong(key, 0L);
		} catch(Exception e) {
			return 0L;
		}
	}
	
	public static boolean getBooleanFromPrefs(String prefsName, String key) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			return prefs.getBoolean(key, false);
		} catch(Exception e) {
			return false;
		}
	}
	
	public static String getStringFromPrefs(String prefsName, String key) {
		if(context == null) {
			return null;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			return prefs.getString(key, null);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static boolean addDataToPrefs(String prefsName, String key, int value) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			
			if(prefs.contains(key)) {
				ed.remove(key);
			}
			
			ed.putInt(key, value);
			return ed.commit();
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean addDataToPrefs(String prefsName, String key, long value) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			
			if(prefs.contains(key)) {
				ed.remove(key);
			}
			
			ed.putLong(key, value);
			return ed.commit();
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean addDataToPrefs(String prefsName, String key, boolean value) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			
			if(prefs.contains(key)) {
				ed.remove(key);
			}
			
			ed.putBoolean(key, value);
			return ed.commit();
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean addDataToPrefs(String prefsName, String key, String value) {
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			
			if(prefs.contains(key)) {
				ed.remove(key);
			}
			
			ed.putString(key, value);
			ed.commit();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean removeVariableFromPrefs(String prefsName, String key) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			
			if(prefs.contains(key)) {
				Editor ed = prefs.edit();
				ed.remove(key);
				return ed.commit();
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean checkPrefs(String prefsName, String key) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			
			return prefs.contains(key);
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean clearPrefs(String prefsName) {
		if(context == null) {
			return false;
		}
		
		try {
			SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			ed.clear();
			return ed.commit();
		} catch(Exception e) {
			return false;
		}
	}
}
