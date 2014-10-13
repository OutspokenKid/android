package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

/**
 * v1.0.1
 * @author HyungGunKim
 *
 * v1.0.1 - Add Schedules
 */
public class StartupInfo {

	private Banner[] banners;
	private MenuColorSet[] menuColorSets;
	private LoadingImageSet loadingImageSet;
	private Popup popup;
	private Notice[] schedules;
	
	public StartupInfo(){}
	
	public StartupInfo(JSONObject objJSON) {
		
		try {
			if(objJSON.has("banner")) {
				JSONArray arJSON = objJSON.getJSONArray("banner");
				
				int length = arJSON.length();
				banners = new Banner[length];
				for(int i=0; i<banners.length; i++) {
					banners[i] = new Banner(arJSON.getJSONObject(i));
				}
			}
			
			if(objJSON.has("color")) {
				JSONArray arJSON = objJSON.getJSONArray("color");
				
				int size = arJSON.length();
				menuColorSets = new MenuColorSet[size];
				for(int i=0; i<size; i++) {
					menuColorSets[i] = new MenuColorSet(arJSON.getJSONObject(i)); 
				}
			}
			
			if(objJSON.has("loading")) {
				loadingImageSet = new LoadingImageSet(objJSON.getJSONObject("loading"));
			}
			
			if(objJSON.has("popup")) {
				popup = new Popup(objJSON.getJSONObject("popup"));
			}
			
			if(objJSON.has("schedule")) {
				
				JSONArray arJSON = objJSON.getJSONArray("schedule");
				
				int size = arJSON.length();
				schedules = new Notice[size];
				for(int i=0; i<size; i++) {
					schedules[i] = new Notice(arJSON.getJSONObject(i));
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public Banner[] getBanners() {
		
		return banners;
	}
	
	public void setBanners(Banner[] banners) {
		
		this.banners = banners;
	}
	
	public MenuColorSet[] getMenuColorSets() {
		return menuColorSets;
	}
	
	public void setMenuColorSets(MenuColorSet[] menuColorSets) {
		this.menuColorSets = menuColorSets;
	}

	public LoadingImageSet getLoadingImageSet() {
		return loadingImageSet;
	}

	public void setLoadingImageSet(LoadingImageSet loadingImageSet) {
		this.loadingImageSet = loadingImageSet;
	}

	public Popup getPopup() {
		
		return popup;
	}

	public void setSchedules(Notice[] schedules) {
		
		this.schedules = schedules;
	}
	
	public Notice[] getSchedules() {
		
		return schedules;
	}
}
