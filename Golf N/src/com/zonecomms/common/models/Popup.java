package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Popup {

	private String content;
	private String bg_img_url;
	private String link_url;
	private String notice_title;
	private int notice_nid;
	private String[] imageUrls;
	
	public Popup(JSONObject objJSON) {

		try {
			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("bg_img_url")) {
				this.bg_img_url = objJSON.getString("bg_img_url");
			}
			
			if(objJSON.has("link_url")) {
				this.link_url = objJSON.getString("link_url");
			}
			
			if(objJSON.has("notice_title")) {
				this.notice_title = objJSON.getString("notice_title");
			}
			
			if(objJSON.has("notice_nid")) {
				this.notice_nid = objJSON.getInt("notice_nid");
			}
			
			if(objJSON.has("medias")) {
				JSONArray arJSON = objJSON.getJSONArray("medias");
				int size = arJSON.length();
				
				if(size > 0) {
					imageUrls = new String[size];
					for(int i=0; i<size; i++) {
						getImageUrls()[i] = arJSON.getJSONObject(i).getString("media_src");
					}
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBg_img_url() {
		return bg_img_url;
	}
	public void setBg_img_url(String bg_img_url) {
		this.bg_img_url = bg_img_url;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public int getNotice_nid() {
		return notice_nid;
	}
	public void setNotice_nid(int notice_nid) {
		this.notice_nid = notice_nid;
	}
	public String[] getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}
}
