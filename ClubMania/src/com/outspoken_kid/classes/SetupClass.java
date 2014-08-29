package com.outspoken_kid.classes;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.webkit.CookieSyncManager;

import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SetupClass {
	
	public static void setupApplication(Activity activity) {
		setupUtils(activity);
        ApplicationManager.getInstance();
        CookieSyncManager.createInstance(activity.getApplicationContext());
        BitmapDownloader.getInstance().setCacheFactory(activity.getApplicationContext());
        activity.getWindow().setFormat(PixelFormat.RGBA_8888);
	}
	
	public static void setupUtils(Activity activity) {

		SharedPrefsUtils.setContext(activity.getApplicationContext());
		ToastUtils.setContext(activity.getApplicationContext());
        ResizeUtils.setBasicValues(activity, 640);
        AppInfoUtils.setAllInfos(activity.getApplicationContext());
	}
}
