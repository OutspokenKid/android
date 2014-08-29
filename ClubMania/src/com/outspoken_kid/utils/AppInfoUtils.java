package com.outspoken_kid.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * v1.0.1
 * @author HyungGunKim
 *
 * v1.0.1 - Add setAddedString()
 */
public class AppInfoUtils {

	private static String user_token;
	private static String app;
	private static String versionName;
	private static int versionCode;
	private static String lang;
	private static String addedString; 
	
	public static void setAllInfos(Context _context) {
		
		PackageInfo i = null;
		
		try {
			
			if(app == null || versionName == null || versionCode == 0) {
				i = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);

				app = i.packageName;
				versionName = i.versionName;
				versionCode = i.versionCode;
			}
			
			if(user_token == null || lang == null) {
				TelephonyManager telephonyManager;
				telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
				
				user_token = makeHash(telephonyManager.getDeviceId());
				lang = Locale.getDefault().getLanguage();
			}
		} catch (NameNotFoundException e) {
		} catch (Exception e) {}
	}
	
	public static String getUserToken() {
		
		return user_token;
	}

	public static String getApp() {
		
		return app;
	}
	
	public static String getLang() {
		return lang;
	}
	
	public static void setAddedString(String string) {
		
		addedString = string;
	}
	public static String getAddedString() {
		
		return addedString;
//		return "app=" + app + "&version_name=" + versionName + "&version_code=" + versionCode + "&user_token=" + user_token + "&lng=" + lang;
	}
	
	public static byte[] getAddedByteArray() {
		
		ByteArrayBuffer ba_buf = new ByteArrayBuffer(getAddedString().length() + 1);
		ba_buf.append(getAddedString().getBytes(), 0, getAddedString().length());
		return ba_buf.toByteArray();
	}
	
	public static String makeHash(String s) {
    	if(s == null || s.equals("")) {
    		return null;
    	}
    	
		MessageDigest m = null; 

		String hash = null;
		try {
			m = MessageDigest.getInstance("SHA1"); 
			m.update(s.getBytes(),0,s.length()); 
			hash = new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();								 
		}
		return hash;											
	}

	public static String getVersionName() {
		return versionName;
	}

	public static void setVersionName(String versionName) {
		AppInfoUtils.versionName = versionName;
	}

	public static int getVersionCode() {
		return versionCode;
	}

	public static void setVersionCode(int versionCode) {
		AppInfoUtils.versionCode = versionCode;
	}
}