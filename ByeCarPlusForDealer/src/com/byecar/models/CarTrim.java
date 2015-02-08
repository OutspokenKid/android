package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class CarTrim extends BCPBaseModel {

	private int id;
	private int brand_id;
	private int modelgroup_id;
	private int model_id;
	private String name;
	private int certified;
	
	public CarTrim() {
		
	}
	
	public CarTrim(JSONObject objJSON) {
		
		super(objJSON);
		
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
			
			if(objJSON.has("modelgroup_id")) {
				this.modelgroup_id = objJSON.getInt("modelgroup_id");
			}
			
			if(objJSON.has("model_id")) {
				this.model_id = objJSON.getInt("model_id");
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

	public int getModelgroup_id() {
		return modelgroup_id;
	}

	public void setModelgroup_id(int modelgroup_id) {
		this.modelgroup_id = modelgroup_id;
	}

	public int getModel_id() {
		return model_id;
	}

	public void setModel_id(int model_id) {
		this.model_id = model_id;
	}
}
