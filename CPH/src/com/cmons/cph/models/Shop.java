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
	
	private int type;
	
	private int id;
	private String name;
	private String phone_number;
	private String owner_id;
	private String owner_name;
	private String owner_phone_number;
	private String corp_reg_number;
	private int favorited_products_cnt;
	private long created_at;
	private int total_visited_cnt;
	private int today_visited_cnt;
	private int favorited_cnt;
	private int status;
	
	private String jsonString;
	
	public Shop() {
	}
	
	public Shop(JSONObject objJSON) {
		
		try {
			setJsonString(objJSON.toString());
			
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
			
			if(objJSON.has("owner_name")) {
				this.owner_name = objJSON.getString("owner_name");
			}
			
			if(objJSON.has("owner_phone_number")) {
				this.owner_phone_number = objJSON.getString("owner_phone_number");
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
			
			if(objJSON.has("total_visited_cnt")) {
				this.total_visited_cnt = objJSON.getInt("total_visited_cnt");
			}
			
			if(objJSON.has("today_visited_cnt")) {
				this.today_visited_cnt = objJSON.getInt("today_visited_cnt");
			}
			
			if(objJSON.has("favorited_cnt")) {
				this.favorited_cnt = objJSON.getInt("favorited_cnt");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
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

	public int getTotal_visited_cnt() {
		return total_visited_cnt;
	}

	public void setTotal_visited_cnt(int total_visited_cnt) {
		this.total_visited_cnt = total_visited_cnt;
	}

	public int getToday_visited_cnt() {
		return today_visited_cnt;
	}

	public void setToday_visited_cnt(int today_visited_cnt) {
		this.today_visited_cnt = today_visited_cnt;
	}

	public int getFavorited_cnt() {
		return favorited_cnt;
	}

	public void setFavorited_cnt(int favorited_cnt) {
		this.favorited_cnt = favorited_cnt;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
