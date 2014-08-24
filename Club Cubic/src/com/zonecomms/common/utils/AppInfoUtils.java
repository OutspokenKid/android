package com.zonecomms.common.utils;

import java.net.URLEncoder;

import com.zonecomms.clubcubic.classes.ZoneConstants;
import com.zonecomms.clubcubic.classes.ZonecommsApplication;

public class AppInfoUtils {
	
	public static int NONE = 0;
	public static int WITHOUT_MEMBER_ID = 1;
	public static int WITHOUT_SB_ID = 2;
	public static int ALL = 3;
	
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
			
			if(mode == NONE) {
				//No.
				
			} else if(mode == WITHOUT_SB_ID && ZonecommsApplication.myInfo != null) {
				//Need member_id.
				str += "&member_id=" + URLEncoder.encode(ZonecommsApplication.myInfo.getMember_id(), "utf-8");
				
			} else if(mode == WITHOUT_MEMBER_ID) {
				//Need sb_id.
				str += "&sb_id=" + ZoneConstants.PAPP_ID;
				
			} else if(mode == ALL) {
				//Need both.
				
				str += "&sb_id=" + ZoneConstants.PAPP_ID;
				
				if(ZonecommsApplication.myInfo != null) {
					str += "&member_id=" + URLEncoder.encode(ZonecommsApplication.myInfo.getMember_id(), "utf-8");
				}
			}
			
			return str;
		} catch(Exception e) {
		}
		
		return null;
	}
}
