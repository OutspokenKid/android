package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class PappInfo {

	private String media_src;
	private String simple_sb_intro;
	private String sb_web_url;
	private String sb_facebook;
	private String sb_latitude;
	private String sb_longitude;
	private String sb_description;
	
	private String[] sub_media_src;
	private Phone[] phones;
	
	public PappInfo() {
	}
	
	public PappInfo(JSONObject objJSON) {
		
		try {
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("simple_sb_intro")) {
				this.simple_sb_intro = objJSON.getString("simple_sb_intro");
			}
			
			if(objJSON.has("sb_web_url")) {
				this.sb_web_url = objJSON.getString("sb_web_url");
			}
			
			if(objJSON.has("sb_facebook")) {
				this.sb_facebook = objJSON.getString("sb_facebook");
			}
			
			if(objJSON.has("sb_latitude")) {
				this.sb_latitude = objJSON.getString("sb_latitude");
			}
			
			if(objJSON.has("sb_longitude")) {
				this.sb_longitude = objJSON.getString("sb_longitude");
			}
			
			if(objJSON.has("sb_description")) {
				this.sb_description = objJSON.getString("sb_description");
			}
			
			if(objJSON.has("sub_media_src")) {
				JSONArray arJSON = objJSON.getJSONArray("sub_media_src");
				
				int size = arJSON.length();
				
				if(size != 0) {
					this.sub_media_src = new String[size];
					for(int i=0; i<size; i++) {

						try {
							sub_media_src[i] = arJSON.getJSONObject(i).getString("url");
						} catch (Exception e) {
							LogUtils.trace(e);
							sub_media_src[i] = null;
						} catch (Error e) {
							LogUtils.trace(e);
							sub_media_src[i] = null;
						}
					}
				}
			}

			if(objJSON.has("phone")) {
				JSONArray arJSON = objJSON.getJSONArray("phone");
				
				int size = arJSON.length();
				
				if(size != 0) {
					phones = new Phone[size];
					
					for(int i=0; i<size; i++) {
						Phone p = new Phone();
						p.sb_tel_name = arJSON.getJSONObject(i).getString("sb_tel_name");
						p.sb_tel = arJSON.getJSONObject(i).getString("sb_tel");
						phones[i] = p;
					}
				}
			}
		} catch(Exception e) {
		}
	}
	
	public String getMedia_src() {
		return media_src;
	}
	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}
	public String getSimple_sb_intro() {
		return simple_sb_intro;
	}
	public void setSimple_sb_intro(String simple_sb_intro) {
		this.simple_sb_intro = simple_sb_intro;
	}
	public String getSb_web_url() {
		return sb_web_url;
	}
	public void setSb_web_url(String sb_web_url) {
		this.sb_web_url = sb_web_url;
	}
	public String getSb_facebook() {
		return sb_facebook;
	}
	public void setSb_facebook(String sb_facebook) {
		this.sb_facebook = sb_facebook;
	}
	public String getSb_latitude() {
		return sb_latitude;
	}
	public void setSb_latitude(String sb_latitude) {
		this.sb_latitude = sb_latitude;
	}
	public String getSb_longitude() {
		return sb_longitude;
	}
	public void setSb_longitude(String sb_longitude) {
		this.sb_longitude = sb_longitude;
	}
	public String getSb_description() {
		return sb_description;
	}
	public void setSb_description(String sb_description) {
		this.sb_description = sb_description;
	}
	public String[] getSub_media_src() {
		return sub_media_src;
	}
	public void setSub_media_src(String[] sub_media_src) {
		this.sub_media_src = sub_media_src;
	}
	public Phone[] getPhones() {
		return phones;
	}
	public void setPhones(Phone[] phones) {
		this.phones = phones;
	}

	public class Phone {
		public String sb_tel_name;
		public String sb_tel; 
	}
}
