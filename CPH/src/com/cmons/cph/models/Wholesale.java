package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Wholesale extends Shop implements Serializable {

	private static final long serialVersionUID = -3343791879247460050L;
	
	private String location;
	private String owner_name;
	private String owner_phone_number;
	private String rep_image_url;
	private int customers_cnt;
	private int favorited_cnt;
	private int products_cnt;
		
	private int[] category_ids;
	
	public Wholesale(JSONObject objJSON) {
		super(objJSON);
		
		try {
			if(objJSON.has("location")) {
				this.location = objJSON.getString("location");
			}
			
			if(objJSON.has("owner_name")) {
				this.owner_name = objJSON.getString("owner_name");
			}
			
			if(objJSON.has("owner_phone_number")) {
				this.owner_phone_number = objJSON.getString("owner_phone_number");
			}
			
			if(objJSON.has("rep_image_url")) {
				this.rep_image_url = objJSON.getString("rep_image_url");
			}
			
			if(objJSON.has("customers_cnt")) {
				this.customers_cnt = objJSON.getInt("customers_cnt");
			}
			
			if(objJSON.has("favorited_cnt")) {
				this.favorited_cnt = objJSON.getInt("favorited_cnt");
			}
			
			if(objJSON.has("products_cnt")) {
				this.products_cnt = objJSON.getInt("products_cnt");
			}
			
			
			if(objJSON.has("category_ids")) {
				
				JSONArray arJSON = objJSON.getJSONArray("category_ids");
				
				int size = arJSON.length();
				category_ids = new int[size];
				for(int i=0; i<size; i++) {
					category_ids[i] = arJSON.getInt(i);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOwner_name() {
		return owner_name;
	}
	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}
	public String getOwner_phone_number() {
		return owner_phone_number;
	}
	public void setOwner_phone_number(String owner_phone_number) {
		this.owner_phone_number = owner_phone_number;
	}
	public String getRep_image_url() {
		return rep_image_url;
	}
	public void setRep_image_url(String rep_image_url) {
		this.rep_image_url = rep_image_url;
	}
	public int getCustomers_cnt() {
		return customers_cnt;
	}
	public void setCustomers_cnt(int customers_cnt) {
		this.customers_cnt = customers_cnt;
	}
	public int getFavorited_cnt() {
		return favorited_cnt;
	}
	public void setFavorited_cnt(int favorited_cnt) {
		this.favorited_cnt = favorited_cnt;
	}
	public int getProducts_cnt() {
		return products_cnt;
	}
	public void setProducts_cnt(int products_cnt) {
		this.products_cnt = products_cnt;
	}
	public int[] getCategory_ids() {
		return category_ids;
	}
	public void setCategory_ids(int[] category_ids) {
		this.category_ids = category_ids;
	}
}
