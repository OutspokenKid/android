package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class PushObject implements Serializable {

	private static final long serialVersionUID = -8039255353290977219L;
	
	public String id;
	public String message;
	public String title;
	public boolean sound;
	public boolean vibrate;
	public String uri;
	
	public PushObject() {
	}
	
	public PushObject(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getString("id");
			}
			
			if(objJSON.has("message")) {
				this.message = objJSON.getString("message");
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("sound")) {
				this.sound = objJSON.getBoolean("sound");
			}
			
			if(objJSON.has("vibrate")) {
				this.vibrate = objJSON.getBoolean("vibrate");
			}
			
			if(objJSON.has("uri")) {
				this.uri = objJSON.getString("uri");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
