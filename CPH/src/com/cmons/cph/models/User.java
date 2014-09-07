package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class User extends BaseModel implements Serializable {

	private static final long serialVersionUID = -4206121495838797414L;
	
	private String id;
	private String type;
	private int role;
	private String name;
	private String platform;
	private String device_token;
	private String phone_number;
	private int wholesale_id;
	private int retail_id;
	private long created_at;
	private int status;
	
	public User(){
	}
	
	public User(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getString("id");
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getString("type");
			}
			
			if(objJSON.has("role")) {
				this.role = objJSON.getInt("role");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("platform")) {
				this.platform = objJSON.getString("platform");
			}
			
			if(objJSON.has("device_token")) {
				this.device_token = objJSON.getString("device_token");
			}
			
			if(objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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
}
