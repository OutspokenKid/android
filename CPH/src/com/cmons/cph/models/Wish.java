package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Wish extends BaseModel implements Serializable {

	private static final long serialVersionUID = 7278134856187095736L;
	
	private int id;
	private int retail_id;
	private int product_id;
	private String color;
	private String size;
	private int amount;
	private long created_at;
	private String product_name;
	private int product_price;
	
	public Wish(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("product_id")) {
				this.product_id = objJSON.getInt("product_id");
			}
			
			if(objJSON.has("color")) {
				this.color = objJSON.getString("color");
			}
			
			if(objJSON.has("size")) {
				this.size = objJSON.getString("size");
			}
			
			if(objJSON.has("amount")) {
				this.amount = objJSON.getInt("amount");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("product_name")) {
				this.product_name = objJSON.getString("product_name");
			}
			
			if(objJSON.has("product_price")) {
				this.product_price = objJSON.getInt("product_price");
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

	public int getProduct_price() {
		return product_price;
	}

	public void setProduct_price(int product_price) {
		this.product_price = product_price;
	}
}
