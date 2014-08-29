package com.outspoken_kid.utils;

import java.util.ArrayList;
import java.util.List;

import com.outspoken_kid.model.AppInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class PackageUtils {

	private static List<RunningTaskInfo> info;
	
	public static boolean checkApplicationInstalled(Context context, String packageName) {
		
		try {
			context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void deleteApplication(Context context, String packageName) {

		try {
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + packageName));
			context.startActivity(intent);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkApplicationRunning(Context context, String className) {
	
	if(info == null || info.size() == 0) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		info = activityManager.getRunningTasks(10);
	}
	
	int size = info.size();
	for(int i=0; i<size; i++) {
		String nowAPP = info.get(i).topActivity.getClassName().toString();
		
		if(nowAPP.contains(className)) {
			return true;
		}
	}
	
	return false;
}
	
	public static ArrayList<AppInfo> getInstalledApps(Context context, boolean isExcludeSystemApp) {
		
		PackageManager pm = context.getPackageManager();
		ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
		
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		int size = packs.size();
	    for(int i=0; i<size; i++) {
	        PackageInfo p = packs.get(i);
	        ApplicationInfo app = p.applicationInfo;
	        
	        if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
	        	//Pass.

	        //it's a system app, not interested
		    } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1 && isExcludeSystemApp) {
		        //Discard this one
		    	continue;
		    	
		    //in this case, it should be a user-installed app
		    } else {
		    	//Pass.
		    }
	        
	        AppInfo appInfo = new AppInfo();
	        String appName = p.applicationInfo.loadLabel(pm).toString();
	        appInfo.setAppName(appName);
	        appInfo.setPackageName(p.packageName);
	        appInfo.setVersionName(p.versionName);
	        appInfo.setVersionCode(p.versionCode);
	        appInfo.setIcon(p.applicationInfo.loadIcon(pm));
	        appInfo.setInitialName(StringUtils.getInitailLetters(appName));
	        appInfos.add(appInfo);
	    }
	    
	    return appInfos;
	}
}
