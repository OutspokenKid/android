package com.cmons.cph.classes;

import android.app.Activity;
import android.graphics.PixelFormat;

import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.ResizeUtils;

public class CphApplication extends OutSpokenApplication {

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static void initWithActivity(Activity activity) {

		RequestManager.initForUsingCookie(activity.getApplicationContext());
		createImageCache(activity.getApplicationContext());
		ResizeUtils.setBasicValues(activity, 720);
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);
	}
}