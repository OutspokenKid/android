package com.zonecomms.common.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;

/**
 * @author HyungGunKim
 * Using at post, reply and messmember_age.
 */
public class Member extends BaseModel {

	private int member_nid;
	private String member_nickname;
	private String media_src;
	private String member_id;
	private String mystory_title;
	private String member_gender;
	private int member_age;
	private int sb_member_nid;
	private int status;
	private String reg_dt;
	
	public Member() {}
	
	public Member(JSONObject objJSON) {
		
		try {
			if(objJSON.has("member_nid")) {
				this.member_nid = objJSON.getInt("member_nid");
			}
			
			if(objJSON.has("member_nickname")) {
				this.member_nickname = objJSON.getString("member_nickname");
			}
			
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("member_id")) {
				this.member_id = objJSON.getString("member_id");
			}
			
			if(objJSON.has("mystory_title")) {
				this.mystory_title = objJSON.getString("mystory_title");
			}
			
			if(objJSON.has("member_gender")) {
				this.member_gender = objJSON.getString("member_gender");
			}
			
			if(objJSON.has("member_age")) {
				this.member_age = objJSON.getInt("member_age");
			}
			
			if(objJSON.has("sb_member_nid")) {
				this.sb_member_nid = objJSON.getInt("sb_member_nid");
				setIndexno(sb_member_nid);
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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
	
	public String getMedia_src() {
		return media_src;
	}
	
	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}
	
	public String getMember_id() {
		return member_id;
	}
	
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	
	public String getMystory_title() {
		return mystory_title;
	}
	
	public void setMystory_title(String mystory_title) {
		this.mystory_title = mystory_title;
	}
	
	public String getMember_gender() {
		return member_gender;
	}
	
	public void setMember_gender(String member_gender) {
		this.member_gender = member_gender;
	}
	
	public int getMember_age() {
		return member_age;
	}
	
	public void setMember_age(int member_age) {
		this.member_age = member_age;
	}

	public int getSb_member_nid() {
		return sb_member_nid;
	}

	public void setSb_member_nid(int sb_member_nid) {
		this.sb_member_nid = sb_member_nid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
}
