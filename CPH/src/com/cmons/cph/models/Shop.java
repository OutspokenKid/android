package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Shop extends BaseModel implements Serializable {

	private static final long serialVersionUID = -125694241420635627L;
	
	public static final int TYPE_WHOLESALE = 0;
	public static final int TYPE_RETAIL_OFFLINE = 1;
	public static final int TYPE_RETAIL_ONLINE = 2;
	
	protected int type;
	
	protected int id;
	protected String name;
	protected String phone_number;
	protected String owner_id;
	protected String corp_reg_number;
	protected int favorited_products_cnt;
	protected long created_at;
	
	public Shop() {
	}
	
	public Shop(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}
			
			if(objJSON.has("owner_id")) {
				this.owner_id = objJSON.getString("owner_id");
			}
			
			if(objJSON.has("corp_reg_number")) {
				this.corp_reg_number = objJSON.getString("corp_reg_number");
			}

			if(objJSON.has("favorited_products_cnt")) {
				this.favorited_products_cnt = objJSON.getInt("favorited_products_cnt");
			}

			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
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
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getCorp_reg_number() {
		return corp_reg_number;
	}
	public void setCorp_reg_number(String corp_reg_number) {
		this.corp_reg_number = corp_reg_number;
	}
	public int getFavorited_products_cnt() {
		return favorited_products_cnt;
	}
	public void setFavorited_products_cnt(int favorited_products_cnt) {
		this.favorited_products_cnt = favorited_products_cnt;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public int getType() {
		
		return type;
	}
	
	public void setType(int type) {
		
		this.type = type;
	}
}
