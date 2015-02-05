package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class CarModelDetailInfo extends BaseModel {

	private int id;
	private int brand_id;
	private int modelgroup_id;
	private int model_id;
	private int trim_id;
	private String full_name;
	private String year_begin;
	private String year_end;
	
	public CarModelDetailInfo() {
		
	}
	
	public CarModelDetailInfo(JSONObject objJSON) {
		
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
			
			if(objJSON.has("model_id")) {
				this.model_id = objJSON.getInt("model_id");
			}
			
			if(objJSON.has("trim_id")) {
				this.trim_id = objJSON.getInt("trim_id");
			}
			
			if(objJSON.has("full_name")) {
				this.full_name = objJSON.getString("full_name");
			}
			
			if(objJSON.has("year_begin")) {
				this.year_begin = objJSON.getString("year_begin");
			}
			
			if(objJSON.has("year_end")) {
				this.year_end = objJSON.getString("year_end");
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

	public int getModel_id() {
		return model_id;
	}

	public void setModel_id(int model_id) {
		this.model_id = model_id;
	}

	public int getTrim_id() {
		return trim_id;
	}

	public void setTrim_id(int trim_id) {
		this.trim_id = trim_id;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
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
}
