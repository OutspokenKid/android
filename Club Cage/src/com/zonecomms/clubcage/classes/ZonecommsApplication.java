package com.zonecomms.clubcage.classes;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.views.holo_dark.HoloConstants;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;

public class ZonecommsApplication extends OutSpokenApplication {
	
	private static MainActivity mainActivity;

	public static void initWithActivity(Activity activity) {
		
		if(activity instanceof MainActivity) {
			mainActivity = (MainActivity) activity;
		}
		
		OutSpokenApplication.initWithActivity(activity);
	}
	
	public static void setupResources(Activity activity) {

		if(activity instanceof MainActivity) {
			mainActivity = (MainActivity) activity; 
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
	
	public static BaseFragment getTopFragment() {
		
		try {
			//메인 페이지 시작 전.
			if(getFragmentsSize() == 0) {
				return null;
				
			//메인 페이지.
			} else if(getFragmentsSize() == 1) {
				return (BaseFragment) mainActivity.getSupportFragmentManager().getFragments().get(0);
				
			//다른 최상단 페이지.
			} else {
				String fragmentTag = mainActivity.getSupportFragmentManager()
			    		.getBackStackEntryAt(mainActivity.getSupportFragmentManager().getBackStackEntryCount() - 1)
			    		.getName();

			    return (BaseFragment) mainActivity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {	
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public static int getFragmentsSize() {
		
		try {
			//메인 실행 전.
			if(mainActivity.getSupportFragmentManager().getFragments() == null) {
				return 0;
				
			//메인 실행 후.
			} else {
				int entrySize = mainActivity.getSupportFragmentManager().getBackStackEntryCount();
				return entrySize + 1;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return 0;
	}
	
	public static void clearFragmentsWithoutMain() {

		int size = getFragmentsSize();	
		
		for(int i=0; i<size; i++) {
			try {
				((BaseFragment)mainActivity.getSupportFragmentManager().getFragments().get(i)).disableExitAnim(false);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		mainActivity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	public static void clearFragmentsWithLastAnim() {

		int size = getFragmentsSize();
		
		for(int i=0; i<size; i++) {
			try {
				if(i == 0) {
					//Do nothing.
				} else if(i == size - 1) {
					((BaseFragment)mainActivity.getSupportFragmentManager().getFragments().get(i)).disableExitAnim(true);
				} else {
					((BaseFragment)mainActivity.getSupportFragmentManager().getFragments().get(i)).disableExitAnim(false);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		mainActivity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
}
