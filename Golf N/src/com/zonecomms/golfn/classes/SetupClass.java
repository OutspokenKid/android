package com.zonecomms.golfn.classes;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.webkit.CookieSyncManager;

import com.outspoken_kid.classes.OutSpokenConstants;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.golfn.R;

public class SetupClass {
	
	public static void setupApplication(Activity activity) {
		setupUtils(activity);
		setupResources(activity);
		
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
	
	public static void setupResources(Activity activity) {
	
		ZoneConstants.PAPP_ID = activity.getString(R.string.sb_id);
		ZoneConstants.DOMAIN = activity.getString(R.string.domain);
		ZoneConstants.BASE_URL = ZoneConstants.DOMAIN + "/externalapi/public/";
		ZoneConstants.URL_FOR_LEAVEMEMBER = ZoneConstants.DOMAIN + "/withdraw/page";
		ZoneConstants.URL_FOR_FIND_ID_AND_PW = ZoneConstants.DOMAIN + "/m_find_info/page";
		ZoneConstants.URL_FOR_CLAUSE1 = ZoneConstants.DOMAIN + "/resource/id_integration.html";
		ZoneConstants.URL_FOR_CLAUSE2 = ZoneConstants.DOMAIN + "/resource/terms.html";
		
		Resources res = activity.getResources();
		OutSpokenConstants.COLOR_HOLO_COVER = res.getColor(R.color.color_button_cover);
		OutSpokenConstants.COLOR_HOLO_TARGET_ON = res.getColor(R.color.color_button_cover);
	}
}
