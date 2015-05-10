package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class AreaForSearch extends BCPBaseModel {

	private int id;
	private String address;
	private String sido;
	private String gugun;
	private String dong;
	
	public AreaForSearch(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("address")) {
				this.address = objJSON.getString("address");
			}
			
			if(objJSON.has("sido")) {
				this.sido = objJSON.getString("sido");
			}
			
			if(objJSON.has("gugun")) {
				this.gugun = objJSON.getString("gugun");
			}
			
			if(objJSON.has("dong")) {
				this.dong = objJSON.getString("dong");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSido() {
		return sido;
	}
	public void setSido(String sido) {
		this.sido = sido;
	}
	public String getGugun() {
		return gugun;
	}
	public void setGugun(String gugun) {
		this.gugun = gugun;
	}
	public String getDong() {
		return dong;
	}
	public void setDong(String dong) {
		this.dong = dong;
	}
}
