package com.zonecomms.common.utils;

import java.net.URLEncoder;

import com.zonecomms.napp.MainActivity;

public class AppInfoUtils {
	
	public static int ALL = 0;
	public static int WITHOUT_MEMBER_ID = 1;
	public static int WITHOUT_SB_ID = 2;
	
	public static String getAppInfo(int mode) {
		
		if(com.outspoken_kid.utils.AppInfoUtils.getAddedString() == null) {
			com.outspoken_kid.utils.AppInfoUtils.setAddedString(
					"ver=" + com.outspoken_kid.utils.AppInfoUtils.getVersionCode() 
	        		+ "&lng=" + com.outspoken_kid.utils.AppInfoUtils.getLang() 
	        		+ "&os=android"
	        		+ "&device=android" 
	        		+ "&device_token=" + com.outspoken_kid.utils.AppInfoUtils.getUserToken());
		}
		
		try {
			String str = com.outspoken_kid.utils.AppInfoUtils.getAddedString();
			
			if(mode == 3) {
				//No.
			} else if(mode == 2) {
				//Need member_id.
				str += "&member_id=" + URLEncoder.encode(MainActivity.myInfo.getMember_id(), "utf-8");
			} else if(mode == 1) {
				//Need sb_id.
				str += "&sb_id=";
			} else {
				//Need both.
				str += "&member_id=" + URLEncoder.encode(MainActivity.myInfo.getMember_id(), "utf-8") +
						"&sb_id=";
			}
			
			return str;
		} catch(Exception e) {
		}
		
		return null;
	}
}
