package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Notice extends BaseModel {

	private int notice_nid;
	private int spot_nid;
	private int sb_nid;
	private String reg_dt;
	private int notice_type;
	private String notice_title;
	private String content;
	/**
	 * First image's url.
	 */
	/**
	 * First image's NID.
	 */
	private int bg_img_nid;
	private Media[] medias;
	
	public Notice(){}
	
	public Notice(JSONObject objJSON) {

		try {
			if(objJSON.has("notice_nid")) {
				this.notice_nid = objJSON.getInt("notice_nid");
				setIndexno(notice_nid);
			}
			
			if(objJSON.has("spot_nid")) {
				this.spot_nid = objJSON.getInt("spot_nid");
			}
			
			if(objJSON.has("sb_nid")) {
				this.sb_nid = objJSON.getInt("sb_nid");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
			
			if(objJSON.has("notice_type")) {
				this.notice_type = objJSON.getInt("notice_type");
			}
			
			if(objJSON.has("notice_title")) {
				this.notice_title = objJSON.getString("notice_title");
			}
			
			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("bg_img_nid")) {
				this.bg_img_nid = objJSON.getInt("bg_img_nid");
			}

			if(objJSON.has("medias")) {
				JSONArray arJSON = objJSON.getJSONArray("medias");
				
				if(arJSON != null && arJSON.length() > 0) {
					int length = arJSON.length();
					medias = new Media[length];
					
					for(int i=0; i<length; i++) {
						Media image = new Media(arJSON.getJSONObject(i));
						medias[i] = image;
					}
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public int getNotice_nid() {
		return notice_nid;
	}

	public void setNotice_nid(int notice_nid) {
		this.notice_nid = notice_nid;
	}

	public int getSpot_nid() {
		return spot_nid;
	}

	public void setSpot_nid(int spot_nid) {
		this.spot_nid = spot_nid;
	}

	public int getSb_nid() {
		return sb_nid;
	}

	public void setSb_nid(int sb_nid) {
		this.sb_nid = sb_nid;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public int getNotice_type() {
		return notice_type;
	}

	public void setNotice_type(int notice_type) {
		this.notice_type = notice_type;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBg_img_nid() {
		return bg_img_nid;
	}

	public void setBg_img_nid(int bg_img_nid) {
		this.bg_img_nid = bg_img_nid;
	}

	public Media[] getMedias() {
		return medias;
	}

	public void setMedias(Media[] medias) {
		this.medias = medias;
	}
}
