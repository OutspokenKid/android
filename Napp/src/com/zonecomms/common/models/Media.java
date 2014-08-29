package com.zonecomms.common.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Media {

	private String media_src;
	private String thumbnail;
	private int width;
	private int height;
	
	public Media(){};
	
	public Media(JSONObject objJSON) {
		
		try {
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("thumbnail")) {
				this.setThumbnail(objJSON.getString("thumbnail"));
			}
			
			if(objJSON.has("width")) {
				this.width = objJSON.getInt("width");
			}
			
			if(objJSON.has("height")) {
				this.height = objJSON.getInt("height");
			}
			
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getMedia_src() {
		return media_src;
	}

	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
