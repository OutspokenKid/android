package com.zonecomms.common.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class MyInfo {

	private String member_id;
	private int member_nid;
	private String member_nickname;
	private String member_media_src;
	private String mystory_title;
	private String member_email;
	private String reg_dt;
	private String mobile_no;
	private String member_birty_yy;
	private String member_birty_md;
	private String admin_grade_kind;
	private boolean admin;
	
	public MyInfo(){};
	
	public boolean setUserInfo(JSONObject objJSON) {
		
		try {
			JSONObject objData = objJSON.getJSONObject("data"); 
			
			if(objData.has("member_id")) {
				this.member_id = objData.getString("member_id");
			}
			
			if(objData.has("member_nid")) {
				this.member_nid = objData.getInt("member_nid");
			}
			
			if(objData.has("member_nickname")) {
				this.member_nickname = objData.getString("member_nickname");
			}
			
			if(objData.has("member_media_src")) {
				this.member_media_src = objData.getString("member_media_src");
			}
			
			if(objData.has("mystory_title")) {
				this.mystory_title = objData.getString("mystory_title");
			}
			
			if(objData.has("member_email")) {
				this.member_email = objData.getString("member_email");
			}
			
			if(objData.has("reg_dt")) {
				this.reg_dt = objData.getString("reg_dt");
			}
			
			if(objData.has("mobile_no")) {
				this.mobile_no = objData.getString("mobile_no");
			}
			
			if(objData.has("member_birty_yy")) {
				this.member_birty_yy = objData.getString("member_birty_yy");
			}
			
			if(objData.has("member_birty_md")) {
				this.member_birty_md = objData.getString("member_birty_md");
			}

			if(objData.has("admin_grade_kind")) {
				this.admin_grade_kind = objData.getString("admin_grade_kind");
			}
			
			if(objJSON.has("admin") 
					&& (objJSON.getString("admin").equals("y") || objJSON.getString("admin").equals("Y"))) {
				this.admin = true;
			} else {
				this.admin = false;
			}
			return true;
		} catch(Exception e) {
			LogUtils.trace(e);
			return false;
		}
	}
	
	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getMember_nid() {
		return member_nid;
	}

	public void setMember_nid(int member_nid) {
		this.member_nid = member_nid;
	}

	public String getMember_nickname() {
		return member_nickname;
	}

	public void setMember_nickname(String member_nickname) {
		this.member_nickname = member_nickname;
	}

	public String getMember_media_src() {
		return member_media_src;
	}

	public void setMember_media_src(String member_media_src) {
		this.member_media_src = member_media_src;
	}

	public String getMystory_title() {
		return mystory_title;
	}

	public void setMystory_title(String mystory_title) {
		this.mystory_title = mystory_title;
	}

	public String getMember_email() {
		return member_email;
	}

	public void setMember_email(String member_email) {
		this.member_email = member_email;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getMember_birty_yy() {
		return member_birty_yy;
	}

	public void setMember_birty_yy(String member_birty_yy) {
		this.member_birty_yy = member_birty_yy;
	}

	public String getMember_birty_md() {
		return member_birty_md;
	}

	public void setMember_birty_md(String member_birty_md) {
		this.member_birty_md = member_birty_md;
	}

	public String getAdmin_grade_kind() {
		return admin_grade_kind;
	}

	public void setAdmin_grade_kind(String admin_grade_kind) {
		this.admin_grade_kind = admin_grade_kind;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
