package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class CarModelGroup extends BaseModel {

	private int id;
	private int brand_id;
	private String name;
	private int certified;
	private int cars_cnt;
	private int priority;
	private String encar_code;
	
	public CarModelGroup() {
		
	}
	
	public CarModelGroup(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("brand_id")) {
				this.brand_id = objJSON.getInt("brand_id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("certified")) {
				this.certified = objJSON.getInt("certified");
			}
			
			if(objJSON.has("cars_cnt")) {
				this.cars_cnt = objJSON.getInt("cars_cnt");
			}
			
			if(objJSON.has("priority")) {
				this.priority = objJSON.getInt("priority");
			}
			
			if(objJSON.has("encar_code")) {
				this.encar_code = objJSON.getString("encar_code");
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
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCertified() {
		return certified;
	}
	public void setCertified(int certified) {
		this.certified = certified;
	}
	public int getCars_cnt() {
		return cars_cnt;
	}
	public void setCars_cnt(int cars_cnt) {
		this.cars_cnt = cars_cnt;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getEncar_code() {
		return encar_code;
	}

	public void setEncar_code(String encar_code) {
		this.encar_code = encar_code;
	}
}
