package com.zonecomms.clubcage.classes;

import android.app.Activity;
import android.content.res.Resources;

import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.views.holo.holo_light.HoloConstants;
import com.zonecomms.clubcage.CircleMainActivity;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.common.models.MyInfo;
import com.zonecomms.common.models.StartupInfo;

public class ZonecommsApplication extends OutSpokenApplication {
	
	public static MyInfo myInfo;
	public static StartupInfo startupInfo;
	private static MainActivity mainActivity;
	private static CircleMainActivity circleMainActivity;

	public static void initWithActivity(Activity activity) {
		
		if(activity instanceof MainActivity) {
			mainActivity = (MainActivity) activity;
		}
		
		OutSpokenApplication.initWithActivity(activity);
	}
	
	public static void setupResources(Activity activity) {

		if(activity instanceof MainActivity) {
			mainActivity = (MainActivity) activity; 
		} else if(activity instanceof CircleMainActivity) {
			circleMainActivity = (CircleMainActivity) activity;
		}
		
		ZoneConstants.PAPP_ID = activity.getString(R.string.sb_id);
		ZoneConstants.DOMAIN = activity.getString(R.string.domain);
		ZoneConstants.BASE_URL = ZoneConstants.DOMAIN + "/externalapi/public/";
		ZoneConstants.URL_FOR_LEAVEMEMBER = ZoneConstants.DOMAIN + "/withdraw/page";
		ZoneConstants.URL_FOR_FIND_ID_AND_PW = ZoneConstants.DOMAIN + "/m_find_info/page";
		ZoneConstants.URL_FOR_CLAUSE1 = ZoneConstants.DOMAIN + "/resource/id_integration.html";
		ZoneConstants.URL_FOR_CLAUSE2 = ZoneConstants.DOMAIN + "/resource/terms.html";
		
		Resources res = activity.getResources();
		HoloConstants.COLOR_HOLO_COVER = res.getColor(R.color.color_button_cover);
		HoloConstants.COLOR_HOLO_TARGET_ON = res.getColor(R.color.color_button_cover);
	}
	
	public static MainActivity getActivity() {
		
		return mainActivity;
	}
	
	public static void setActivity(MainActivity mainActivity) {
		
		ZonecommsApplication.mainActivity = mainActivity;
	}
	
	public static CircleMainActivity getCircleActivity() {
		
		return circleMainActivity;
	}
	
	public static void setCircleMainActivity(CircleMainActivity circleMainActivity) {
		
		ZonecommsApplication.circleMainActivity = circleMainActivity;
	}
}
