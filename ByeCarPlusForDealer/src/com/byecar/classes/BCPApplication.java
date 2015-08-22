package com.byecar.classes;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PixelFormat;

import com.byecar.byecarplusfordealer.R;
import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.HoloConstants;

public class BCPApplication extends OutSpokenApplication {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Resources res = getResources();
		
		HoloConstants.COLOR_HOLO_TARGET_ON = res.getColor(R.color.holo_target_on);
		HoloConstants.COLOR_HOLO_TARGET_OFF = res.getColor(R.color.holo_target_off);
		HoloConstants.COLOR_HOLO_TEXT = res.getColor(R.color.holo_text);
		HoloConstants.COLOR_HOLO_TEXT_HINT = res.getColor(R.color.holo_text_hint);
	}
	
	public static void initWithActivity(Activity activity) {

		RequestManager.initForUsingCookie(activity.getApplicationContext());
		createImageCache(activity.getApplicationContext());
		ResizeUtils.setBasicValues(activity, 640);
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);
	}
}
