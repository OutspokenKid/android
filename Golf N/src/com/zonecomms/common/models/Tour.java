package com.zonecomms.common.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Tour extends BaseModel {
	
	private String sb_nickname;
	private String sb_id;
	private String media_src;
	private String sb_description;
	private int sb_nid;
	private String reg_dt;
	
	public Tour() {}
	
	public Tour(JSONObject objJSON) {
		
		try {
			if(objJSON.has("sb_nickname")) {
				this.sb_nickname = objJSON.getString("sb_nickname");
			}

			if(objJSON.has("sb_id")) {
				this.sb_id = objJSON.getString("sb_id");
			} else {
				sb_id = "";
			}
			
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("sb_description")) {
				this.sb_description = objJSON.getString("sb_description");
			}
			
			if(objJSON.has("sb_nid")) {
				this.sb_nid = objJSON.getInt("sb_nid");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public String getSb_nickname() {
		return sb_nickname;
	}

	public void setSb_nickname(String sb_nickname) {
		this.sb_nickname = sb_nickname;
	}
	
	public String getSb_id() {
		return sb_id;
	}

	public void setSb_id(String sb_id) {
		this.sb_id = sb_id;
	}

	public String getMedia_src() {
		return media_src;
	}

	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}

	public int getSb_nid() {
		return sb_nid;
	}

	public void setSb_nid(int sb_nid) {
		this.sb_nid = sb_nid;
	}

	public String getSb_description() {
		return sb_description;
	}

	public void setSb_description(String sb_description) {
		this.sb_description = sb_description;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
}
