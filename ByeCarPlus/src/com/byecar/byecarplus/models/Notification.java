package com.byecar.byecarplus.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Notification extends BaseModel {

	private int id;
	private int receiver_id;
	private String message;
	private String uri;
	private String img_url;
	private long created_at;
	private long pushed_at;
	private long read_at;
	
	public Notification() {
		
	}
	
	public Notification(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
				setIndexno(id);
			}
			
			if(objJSON.has("receiver_id")) {
				this.receiver_id = objJSON.getInt("receiver_id");
			}
			
			if(objJSON.has("message")) {
				this.message = objJSON.getString("message");
			}
			
			if(objJSON.has("uri")) {
				this.uri = objJSON.getString("uri");
			}
			
			if(objJSON.has("img_url")) {
				this.img_url = objJSON.getString("img_url");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("pushed_at")) {
				this.pushed_at = objJSON.getLong("pushed_at");
			}
			
			if(objJSON.has("read_at")) {
				this.read_at = objJSON.getLong("read_at");
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
	public int getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public long getPushed_at() {
		return pushed_at;
	}
	public void setPushed_at(long pushed_at) {
		this.pushed_at = pushed_at;
	}
	public long getRead_at() {
		return read_at;
	}
	public void setRead_at(long read_at) {
		this.read_at = read_at;
	}
}
