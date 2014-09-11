package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Customer extends BaseModel implements Serializable {

	private static final long serialVersionUID = -3977469842607329854L;
	
	private int id;
	private int wholesale_id;
	private int retail_id;
	private int status;
	private long created_at;
	private String name;
	private int type;
	private String phone_number;
	private String address;
	private String mall_url;
	private String corp_reg_number;
	private String owner_id;
	private String owner_name;
	private String owner_phone_number;
	private int wholesales_cnt;
	private int favorite_wholesales_cnt;
	private int favorite_products_cnt;

	public Customer() {
	}
	
	public Customer(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getInt("type");
			}
			
			if(objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}
			
			if(objJSON.has("address")) {
				this.address = objJSON.getString("address");
			}
			
			if(objJSON.has("mall_url")) {
				this.mall_url = objJSON.getString("mall_url");
			}
			
			if(objJSON.has("corp_reg_number")) {
				this.corp_reg_number = objJSON.getString("corp_reg_number");
			}
			
			if(objJSON.has("owner_id")) {
				this.owner_id = objJSON.getString("owner_id");
			}
			
			if(objJSON.has("owner_name")) {
				this.owner_name = objJSON.getString("owner_name");
			}
			
			if(objJSON.has("owner_phone_number")) {
				this.owner_phone_number = objJSON.getString("owner_phone_number");
			}
			
			if(objJSON.has("wholesales_cnt")) {
				this.wholesales_cnt = objJSON.getInt("wholesales_cnt");
			}
			
			if(objJSON.has("favorite_wholesales_cnt")) {
				this.favorite_wholesales_cnt = objJSON.getInt("favorite_wholesales_cnt");
			}
			
			if(objJSON.has("favorite_products_cnt")) {
				this.favorite_products_cnt = objJSON.getInt("favorite_products_cnt");
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
	public int getWholesale_id() {
		return wholesale_id;
	}
	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}
	public int getRetail_id() {
		return retail_id;
	}
	public void setRetail_id(int retail_id) {
		this.retail_id = retail_id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMall_url() {
		return mall_url;
	}
	public void setMall_url(String mall_url) {
		this.mall_url = mall_url;
	}
	public String getCorp_reg_number() {
		return corp_reg_number;
	}
	public void setCorp_reg_number(String corp_reg_number) {
		this.corp_reg_number = corp_reg_number;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
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
	public int getWholesales_cnt() {
		return wholesales_cnt;
	}
	public void setWholesales_cnt(int wholesales_cnt) {
		this.wholesales_cnt = wholesales_cnt;
	}
	public int getFavorite_wholesales_cnt() {
		return favorite_wholesales_cnt;
	}
	public void setFavorite_wholesales_cnt(int favorite_wholesales_cnt) {
		this.favorite_wholesales_cnt = favorite_wholesales_cnt;
	}
	public int getFavorite_products_cnt() {
		return favorite_products_cnt;
	}
	public void setFavorite_products_cnt(int favorite_products_cnt) {
		this.favorite_products_cnt = favorite_products_cnt;
	}
}
