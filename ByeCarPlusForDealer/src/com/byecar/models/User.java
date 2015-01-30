package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class User extends BaseModel {

	private int id;
	private int role;
	private String email;
	private String profile_img_url;
	private String nickname;
	private String name;
	private String phone_number;
	private String address;
	private int dealer_id;
	private int status;
	private int blocked_until;
	private int to_get_pushed; 
	private long created_at;
	
	public User() {
		
	}
	
	public User(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
				setIndexno(id);
			}
			
			if(objJSON.has("role")) {
				this.role = objJSON.getInt("role");
			}
			
			if(objJSON.has("email")) {
				this.email = objJSON.getString("email");
			}
			
			if(objJSON.has("profile_img_url")) {
				this.profile_img_url = objJSON.getString("profile_img_url");
			}
			
			if(objJSON.has("nickname")) {
				this.nickname = objJSON.getString("nickname");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}
			
			if(objJSON.has("address")) {
				this.address = objJSON.getString("address");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("blocked_until")) {
				this.blocked_until = objJSON.getInt("blocked_until");
			}
			
			if(objJSON.has("to_get_pushed")) {
				this.to_get_pushed = objJSON.getInt("to_get_pushed");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfile_img_url() {
		return profile_img_url;
	}
	public void setProfile_img_url(String profile_img_url) {
		this.profile_img_url = profile_img_url;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(int dealer_id) {
		this.dealer_id = dealer_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getBlocked_until() {
		return blocked_until;
	}
	public void setBlocked_until(int blocked_until) {
		this.blocked_until = blocked_until;
	}
	public int getTo_get_pushed() {
		return to_get_pushed;
	}
	public void setTo_get_pushed(int to_get_pushed) {
		this.to_get_pushed = to_get_pushed;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
}
