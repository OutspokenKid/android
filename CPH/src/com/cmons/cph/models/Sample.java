package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Sample extends BaseModel implements Serializable {
		
	private static final long serialVersionUID = -5981519056232983287L;
	
	private int id;
	private int retail_id;
	private int product_id;
	private int wholesale_id;
	private String color;
	private String size;
	private int status;
	private long created_at;
	private String product_name;
	private String retail_name;
	private String retail_phone_number;
	private String retail_owner_name;
	private String wholesale_name;
	private String wholesale_phone_number;
	private String wholesale_owner_name;
	
	public Sample() {
	}
	
	public Sample(JSONObject objJSON) {
		
		try {
			LogUtils.log("###\n1\n1\n1.Sample.  \n" + objJSON.toString());
			
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("product_id")) {
				this.product_id = objJSON.getInt("product_id");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("color")) {
				this.color = objJSON.getString("color");
			}
			
			if(objJSON.has("size")) {
				this.size = objJSON.getString("size");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("product_name")) {
				this.product_name = objJSON.getString("product_name");
			}
			
			if(objJSON.has("retail_name")) {
				this.retail_name = objJSON.getString("retail_name");
			}
			
			if(objJSON.has("retail_phone_number")) {
				this.retail_phone_number = objJSON.getString("retail_phone_number");
			}
			
			if(objJSON.has("retail_owner_name")) {
				this.retail_owner_name = objJSON.getString("retail_owner_name");
			}
			
			if(objJSON.has("wholesale_name")) {
				this.wholesale_name = objJSON.getString("wholesale_name");
			}
			
			if(objJSON.has("wholesale_phone_number")) {
				this.wholesale_phone_number = objJSON.getString("wholesale_phone_number");
			}
			
			if(objJSON.has("wholesale_owner_name")) {
				this.wholesale_owner_name = objJSON.getString("wholesale_owner_name");
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
	public int getRetail_id() {
		return retail_id;
	}
	public void setRetail_id(int retail_id) {
		this.retail_id = retail_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getWholesale_id() {
		return wholesale_id;
	}
	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getRetail_name() {
		return retail_name;
	}
	public void setRetail_name(String retail_name) {
		this.retail_name = retail_name;
	}
	public String getRetail_phone_number() {
		return retail_phone_number;
	}
	public void setRetail_phone_number(String retail_phone_number) {
		this.retail_phone_number = retail_phone_number;
	}
	public String getRetail_owner_name() {
		return retail_owner_name;
	}
	public void setRetail_owner_name(String retail_owner_name) {
		this.retail_owner_name = retail_owner_name;
	}
	public String getWholesale_name() {
		return wholesale_name;
	}
	public void setWholesale_name(String wholesale_name) {
		this.wholesale_name = wholesale_name;
	}
	public String getWholesale_phone_number() {
		return wholesale_phone_number;
	}
	public void setWholesale_phone_number(String wholesale_phone_number) {
		this.wholesale_phone_number = wholesale_phone_number;
	}
	public String getWholesale_owner_name() {
		return wholesale_owner_name;
	}
	public void setWholesale_owner_name(String wholesale_owner_name) {
		this.wholesale_owner_name = wholesale_owner_name;
	}
}
