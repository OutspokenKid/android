package com.cmons.cph.classes;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentManager;

import com.cmons.cph.WholesaleActivity;
import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class CphApplication extends OutSpokenApplication {

	public static void initWithActivity(Activity activity) {
		
		ResizeUtils.setBasicValues(activity, 720);
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);
	}
}