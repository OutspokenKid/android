package com.zonecomms.common.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Banner {

	private int status;
	private String ad_nid;
	private String img_url;
	private String reg_id;
	private String reg_date;
	private String target_link;
	
	public Banner(){};
	
	public Banner(JSONObject objJSON) {
		
		try {
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("ad_nid")) {
				this.ad_nid = objJSON.getString("ad_nid");
			}
			
			if(objJSON.has("img_url")) {
				this.img_url = objJSON.getString("img_url");
			}
			
			if(objJSON.has("reg_id")) {
				this.reg_id = objJSON.getString("reg_id");
			}
			
			if(objJSON.has("reg_date")) {
				this.reg_date = objJSON.getString("reg_date");
			}
			
			if(objJSON.has("target_link")) {
				this.target_link = objJSON.getString("target_link");
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAd_nid() {
		return ad_nid;
	}
	public void setAd_nid(String ad_nid) {
		this.ad_nid = ad_nid;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getTarget_link() {
		return target_link;
	}
	public void setTarget_link(String target_link) {
		this.target_link = target_link;
	}
}
