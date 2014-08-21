package com.cmons.cph.classes;

import android.app.Activity;
import android.graphics.PixelFormat;

import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.ResizeUtils;

public class CphApplication extends OutSpokenApplication {

	public static void initWithActivity(Activity activity) {
		
		ResizeUtils.setBasicValues(activity, 720);
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);

		RequestManager.initForUsingCookie(activity.getApplicationContext());
	}
}