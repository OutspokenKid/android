package com.zonecomms.common.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;

/**
 * 
 * @author HyungGunKim
 * 
 *sb_id : 모임 아이디
 *sb_nickname : 모임 닉네임
 *sb_nid : 모임 번호
 *media_src : 모임 이미지
 *sb_description : 모임 설명
 *sb_public : 1 (공개) , 2(비공개-승인형 모임)
 *member_cnt : 모임 가입자수
 *reg_dt : 개설일
 *reg_id : 개설자 ID
 *reg_nickname : 개설자 닉네임
 *member_sb_role : 8(생성자), 9(일반회원), 0(회원아님)
 *member_sb_status : 0 (승인 대기전), 1 (회원)
 */
public class Gethering extends BaseModel {

	private String sb_id;
	private String sb_nickname;
	private int sb_nid;
	private String media_src;
	private String sb_description;
	private int sb_public;
	private int member_cnt;
	private int posted_cnt;
	//reg_dt는 BaseModel에 정의되어있음.
	private String reg_id;
	private String reg_nickname;
	private String reg_dt;
	
	/**
	 * 0 : 회원 아님.
	 * 9 : 일반 회원.
	 * 8 : 생성자.
	 */
	private int member_sb_role;
	
	/**
	 * 0 : 승인 대기 전.
	 * 1 : 회원.
	 */
	private int member_sb_status;
	private boolean owner; 
	private String reg_image;
	
	public Gethering(JSONObject objJSON) {
	
		try {
			if(objJSON.has("sb_id")) {
				this.sb_id = objJSON.getString("sb_id");
			}
			
			if(objJSON.has("sb_nickname")) {
				this.sb_nickname = objJSON.getString("sb_nickname");
			}
			
			if(objJSON.has("sb_nid")) {
				this.sb_nid = objJSON.getInt("sb_nid");
				setIndexno(sb_nid);
			}
			
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("sb_description")) {
				this.sb_description = objJSON.getString("sb_description");
			}
			
			if(objJSON.has("sb_public")) {
				this.sb_public = objJSON.getInt("sb_public");
			}
			
			if(objJSON.has("member_cnt")) {
				this.member_cnt = objJSON.getInt("member_cnt");
			}
			
			if(objJSON.has("posted_cnt")) {
				this.posted_cnt = objJSON.getInt("posted_cnt");
			}
			
			if(objJSON.has("reg_dt")) {
				this.setReg_dt(objJSON.getString("reg_dt"));
			}
			
			if(objJSON.has("reg_id")) {
				this.reg_id = objJSON.getString("reg_id");
			}
			
			if(objJSON.has("reg_nickname")) {
				this.reg_nickname = objJSON.getString("reg_nickname");
			}
			
			if(objJSON.has("member_sb_role")) {
				this.member_sb_role = objJSON.getInt("member_sb_role");
				
				if(member_sb_role == 8) {
					this.owner = true;
				} else {
					this.owner = false;
				}
			}
			
			if(objJSON.has("member_sb_status")) {
				this.member_sb_status = objJSON.getInt("member_sb_status");
			}
			
			if(objJSON.has("reg_image")) {
				this.reg_image = objJSON.getString("reg_image");
			}
		} catch(Exception e) {
		}
	}

	public String getSb_id() {
		return sb_id;
	}

	public void setSb_id(String sb_id) {
		this.sb_id = sb_id;
	}

	public String getSb_nickname() {
		return sb_nickname;
	}

	public void setSb_nickname(String sb_nickname) {
		this.sb_nickname = sb_nickname;
	}

	public int getSb_nid() {
		return sb_nid;
	}

	public void setSb_nid(int sb_nid) {
		this.sb_nid = sb_nid;
	}

	public String getMedia_src() {
		return media_src;
	}

	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}

	public String getSb_description() {
		return sb_description;
	}

	public void setSb_description(String sb_description) {
		this.sb_description = sb_description;
	}

	public int getSb_public() {
		return sb_public;
	}

	public void setSb_public(int sb_public) {
		this.sb_public = sb_public;
	}

	public int getMember_cnt() {
		return member_cnt;
	}

	public void setMember_cnt(int member_cnt) {
		this.member_cnt = member_cnt;
	}

	public String getReg_id() {
		return reg_id;
	}

	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}

	public String getReg_nickname() {
		return reg_nickname;
	}

	public void setReg_nickname(String reg_nickname) {
		this.reg_nickname = reg_nickname;
	}

	public int getMember_sb_role() {
		return member_sb_role;
	}

	public void setMember_sb_role(int member_sb_role) {
		this.member_sb_role = member_sb_role;
	}

	public int getMember_sb_status() {
		return member_sb_status;
	}

	public void setMember_sb_status(int member_sb_status) {
		this.member_sb_status = member_sb_status;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public String getReg_image() {
		return reg_image;
	}

	public void setReg_image(String reg_image) {
		this.reg_image = reg_image;
	}

	public int getPosted_cnt() {
		return posted_cnt;
	}

	public void setPosted_cnt(int posted_cnt) {
		this.posted_cnt = posted_cnt;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
}
