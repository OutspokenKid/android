package com.outspoken_kid.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.outspoken_kid.utils.StringUtils;

/**
 * v1.0.1
 * 
 * @author HyungGunKim
 *
 * v1.0.1 - Add call(Context, String)
 */
public class IntentUtils {

	public static boolean invokeApp(Context context, String packageName) {

		try {
			PackageManager pm = context.getPackageManager();
			Intent i = pm.getLaunchIntentForPackage(packageName);
			context.startActivity(i);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean invokeApp(Context context, String packageName, String dataString) {

		try {
			PackageManager pm = context.getPackageManager();
			Intent i = pm.getLaunchIntentForPackage(packageName);
			
			if(!StringUtils.isEmpty(dataString)) {
				i.setData(Uri.parse(dataString));
			}

			context.startActivity(i);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean invokeApp(Context context, String packageName, 
			String id, String pw, String dataString) {

		try {
			PackageManager pm = context.getPackageManager();
			Intent i = pm.getLaunchIntentForPackage(packageName);
			
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(pw)) {
				i.putExtra("id", id);
				i.putExtra("pw", pw);
			}
			
			if(!StringUtils.isEmpty(dataString)) {
				i.setData(Uri.parse(dataString));
			}

			context.startActivity(i);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean showMarket(Context context, String packageName) {
		
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));  
			context.startActivity(intent);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean showMarket(Context context, Uri uri) {
		
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
			context.startActivity(intent);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean call(Context context, String phoneNumber) {
		
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
			context.startActivity(intent);
			return true;
		} catch(Exception e) {
		}
		
		return false;
	}
}
