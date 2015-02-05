package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Brand extends BaseModel {

	private int id;
	private String name;
	private String country;
	private int certified;
	private String img_url;
	private String order_no;
	private String cars_cnt;
	
	public Brand() {
		
	}
	
	public Brand(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("country")) {
				this.country = objJSON.getString("country");
			}
			
			if(objJSON.has("certified")) {
				this.certified = objJSON.getInt("certified");
			}
			
			if(objJSON.has("img_url")) {
				this.img_url = objJSON.getString("img_url");
			}
			
			if(objJSON.has("order_no")) {
				this.order_no = objJSON.getString("order_no");
			}
			
			if(objJSON.has("cars_cnt")) {
				this.cars_cnt = objJSON.getString("cars_cnt");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getCertified() {
		return certified;
	}

	public void setCertified(int certified) {
		this.certified = certified;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getCars_cnt() {
		return cars_cnt;
	}

	public void setCars_cnt(String cars_cnt) {
		this.cars_cnt = cars_cnt;
	}
}
