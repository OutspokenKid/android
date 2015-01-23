package com.byecar.byecarplusfordealer.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class CarModel extends BaseModel {

	private int id;
	private int brand_id;
	private int modelgroup_id;
	private String name;
	private int certified;
	private String year_begin;
	private String year_end;
	private int cars_cnt;
	private int priority;
	
	public CarModel() {
		
	}
	
	public CarModel(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("brand_id")) {
				this.brand_id = objJSON.getInt("brand_id");
			}
			
			if(objJSON.has("modelgroup_id")) {
				this.modelgroup_id = objJSON.getInt("modelgroup_id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("certified")) {
				this.certified = objJSON.getInt("certified");
			}
			
			if(objJSON.has("year_begin")) {
				this.year_begin = objJSON.getString("year_begin");
			}
			
			if(objJSON.has("year_end")) {
				this.year_end = objJSON.getString("year_end");
			}
			
			if(objJSON.has("cars_cnt")) {
				this.cars_cnt = objJSON.getInt("cars_cnt");
			}
			
			if(objJSON.has("priority")) {
				this.priority = objJSON.getInt("priority");
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
	public int getModelgroup_id() {
		return modelgroup_id;
	}
	public void setModelgroup_id(int modelgroup_id) {
		this.modelgroup_id = modelgroup_id;
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
	public String getYear_begin() {
		return year_begin;
	}
	public void setYear_begin(String year_begin) {
		this.year_begin = year_begin;
	}
	public String getYear_end() {
		return year_end;
	}
	public void setYear_end(String year_end) {
		this.year_end = year_end;
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
}
