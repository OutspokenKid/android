package com.zonecomms.golfn.classes;

import java.util.ArrayList;

import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.golfn.MainActivity;

import android.app.Activity;

/**
 * v1.0.0
 * 
 * @author HyungGunKim
 *
 */
public class ApplicationManager {

	protected static ApplicationManager applicationManager;
	protected ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
	protected ArrayList<Activity> activities = new ArrayList<Activity>();
	protected MainActivity activity;
	
	public ArrayList<BaseFragment> getFragments() {

		try {
			if(applicationManager != null) {
				return applicationManager.fragments;
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public void addFragment(BaseFragment fragment) {
		
		try {
			fragments.add(fragment);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static void removeFragment(BaseFragment fragment) {

		try {
			if(applicationManager != null
					&& applicationManager.fragments.contains(fragment)) {
				applicationManager.fragments.remove(fragment);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static void clearFragments() {
		
		applicationManager.activity.clearFragments();
		applicationManager.fragments.clear();
	}
	
	public static void clearFragmentsWithoutMain() {
		
		applicationManager.activity.clearFragmentsWithoutMain();
		
		int size = applicationManager.fragments.size();
		for(int i=size-1; i>0; i--) {
			removeFragment(applicationManager.fragments.get(i));
		}
	}
	
	public static void clearFragmentsForSideMenu() {
		
		applicationManager.activity.clearFragmentsForSideMenu();
		
		int size = applicationManager.fragments.size();
		for(int i=size-1; i>0; i--) {
			removeFragment(applicationManager.fragments.get(i));
		}
	}
	
	public static BaseFragment getTopFragment() {
		
		try {
			if(applicationManager != null) {
				if(applicationManager.fragments.size() != 0) {
					return applicationManager.fragments.get(applicationManager.fragments.size() - 1);
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public static int getFragmentsSize() {
		
		if(applicationManager != null) {
			return applicationManager.getFragments().size();
		} else {
			return 0;
		}
	}

	public ArrayList<Activity> getActivities() {
		
		try {
			return activities;
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public void addActivity(Activity activity) {
		
		try {
			activities.add(activity);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static void removeActivity(Activity activity) {
		
		try {
			if(applicationManager != null) {
				if(applicationManager.activities.contains(activity)) {
					applicationManager.activities.remove(activity);
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static void clearActivities() {
		
		try {
			if(applicationManager != null) {
				for(int i=applicationManager.activities.size() - 1; i>=0; i--) {
					applicationManager.activities.get(i).finish();
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static ApplicationManager getInstance() {
		
		try {
			if(applicationManager == null) {
				applicationManager = new ApplicationManager();
			}
			
			return applicationManager;
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public void setActivity(MainActivity activity) {
		this.activity = activity;
	}
	
	public MainActivity getActivity() {
		return activity;
	}

	public static void refreshTopPage() {
		
		try {
			getTopFragment().onRefreshPage();
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public static String getDownloadKeyFromTopFragment() {
		
		try {
			if(applicationManager.fragments != null 
					&& applicationManager.fragments.size() > 0) {
				return applicationManager.fragments
						.get(applicationManager.fragments.size() - 1).getDownloadKey();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
}
