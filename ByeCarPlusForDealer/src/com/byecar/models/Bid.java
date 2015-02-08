package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Bid extends BCPBaseModel {

//	"id": "3",
//	"onsalecar_id": "13",
//	"dealer_id": "4",
//	"price": "50000000",
//	"created_at": "1418970844",
//	"status": "1",
//	"dealer_name": "김민상",
//	"dealer_profile_img_url": "abc"
	
	private int id;
	private int onsalecar_id;
	private int dealer_id;
	private long price;
	private long created_at;
	private int status;
	private String dealer_name;
	private String dealer_profile_img_url;
	private String dealer_address;
	private int dealer_level;
	
	public Bid() {
		
	}
	
	public Bid(JSONObject objJSON) {
		
		super(objJSON);
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("onsalecar_id")) {
				this.onsalecar_id = objJSON.getInt("onsalecar_id");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}
			
			if(objJSON.has("price")) {
				this.price = objJSON.getLong("price");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("dealer_name")) {
				this.dealer_name = objJSON.getString("dealer_name");
			}
			
			if(objJSON.has("dealer_profile_img_url")) {
				this.dealer_profile_img_url = objJSON.getString("dealer_profile_img_url");
			}

			if(objJSON.has("dealer_address")) {
				this.dealer_address = objJSON.getString("dealer_address");
			}
			
			if(objJSON.has("dealer_level")) {
				this.dealer_level = objJSON.getInt("dealer_level");
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
	public int getOnsalecar_id() {
		return onsalecar_id;
	}
	public void setOnsalecar_id(int onsalecar_id) {
		this.onsalecar_id = onsalecar_id;
	}
	public int getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(int dealer_id) {
		this.dealer_id = dealer_id;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getDealer_profile_img_url() {
		return dealer_profile_img_url;
	}
	public void setDealer_profile_img_url(String dealer_profile_img_url) {
		this.dealer_profile_img_url = dealer_profile_img_url;
	}

	public String getDealer_address() {
		return dealer_address;
	}

	public void setDealer_address(String dealer_address) {
		this.dealer_address = dealer_address;
	}

	public int getDealer_level() {
		return dealer_level;
	}

	public void setDealer_level(int dealer_level) {
		this.dealer_level = dealer_level;
	}
}
