package com.outspoken_kid.classes;

import java.util.ArrayList;

import com.zonecomms.clubmania.MainActivity;
import com.zonecomms.clubmania.fragments.MainPage;

import android.app.Activity;

/**
 * v1.0.0
 * 
 * @author HyungGunKim
 *
 */
public class ApplicationManager {

	private static ApplicationManager applicationManager;
	private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	private MainActivity mainActivity;
	
	public ArrayList<BaseFragment> getFragments() {

		try {
			if(applicationManager != null) {
				return applicationManager.fragments;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void addFragment(BaseFragment fragment) {
		
		try {
			fragments.add(fragment);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeFragment(BaseFragment fragment) {

		try {
			if(applicationManager != null
					&& applicationManager.fragments.contains(fragment)) {
				applicationManager.fragments.remove(fragment);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clearFragments() {
		
		try {
			if(applicationManager != null) {
				for(int i=applicationManager.fragments.size() - 1; i>=0; i--) {
					applicationManager.fragments.get(i).finish(false, false);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clearFragmentsWithoutMain() {
		
		try {
			if(applicationManager != null) {
				for(int i=applicationManager.fragments.size() - 1; i>0; i--) {
					
					if(i!=1) {
						applicationManager.fragments.get(i).finish(false, false);
					} else {
						applicationManager.fragments.get(i).finish(false, true);
					}
				}

				applicationManager.mainActivity.showTopFragment();
				((MainPage) applicationManager.getFragments().get(0)).setScrollToTop();
			}
		} catch(Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void addActivity(Activity activity) {
		
		try {
			activities.add(activity);
		} catch(Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	public static ApplicationManager getInstance() {
		
		try {
			if(applicationManager == null) {
				applicationManager = new ApplicationManager();
			}
			
			return applicationManager;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public static void refreshTopPage() {
		
		try {
			getTopFragment().onRefreshPage();
		} catch(Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
		
		return null;
	}
}
