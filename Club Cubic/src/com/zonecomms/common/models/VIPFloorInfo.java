package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class VIPFloorInfo {

	private String[] link_datas;
	private String floor;
	private String title;
	private String link_nid;
	private String reg_dt;
	private String[] thumbnail;
	
	public VIPFloorInfo() {
	}
	
	public VIPFloorInfo(JSONObject objJSON) {
		
		try {
			if(objJSON.has("link_datas")) {
				
				JSONArray arJSON = objJSON.getJSONArray("link_datas");
				
				int size = arJSON.length();
				link_datas = new String[size];
				for(int i=0; i<size; i++) {
					link_datas[i] = arJSON.getString(i);
				}
			}
			
			if(objJSON.has("floor")) {
				this.floor = objJSON.getString("floor");
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("link_nid")) {
				this.link_nid = objJSON.getString("link_nid");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
			
			if(objJSON.has("thumbnail")) {
				
				JSONArray arJSON = objJSON.getJSONArray("thumbnail");
				
				int size = arJSON.length();
				thumbnail = new String[size];
				for(int i=0; i<size; i++) {
					thumbnail[i] = arJSON.getString(i);
				}
			}
		} catch(Exception e) {
		}
	}
	
	public String[] getLink_datas() {
		return link_datas;
	}
	public void setLink_datas(String[] link_datas) {
		this.link_datas = link_datas;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink_nid() {
		return link_nid;
	}
	public void setLink_nid(String link_nid) {
		this.link_nid = link_nid;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
