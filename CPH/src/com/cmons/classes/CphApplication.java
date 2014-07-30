package com.cmons.classes;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentManager;

import com.cmons.cph.MainActivity;
import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class CphApplication extends OutSpokenApplication {

	private static MainActivity mainActivity;
	
	public static void initWithActivity(Activity activity) {
		
		ResizeUtils.setBasicValues(activity, 720);
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);
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
			((BaseFragment)mainActivity.getSupportFragmentManager().getFragments().get(i)).disableExitAnim();
		}
		
		mainActivity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

}
