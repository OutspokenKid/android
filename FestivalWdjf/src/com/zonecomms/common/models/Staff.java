package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class Staff {

	private String member_id;
	private String reg_dt;
	private String sb_id;
	private String[] profile_image;
	private String member_email;
	private String chg_id;
	private int sb_nid;
	private String reg_id;
	private int staff_nid;
	private String url_kakao;
	private String profile;
	private String phone;
	private String main_image;
	private String url_n_home;
	private String url_webpage;
	private String url_facebook;
	private String url_twitter;
	private String url_youtube;
	private String url_soundCloud;
	private String url_instagram;
	private String url_kakaoStory;
	private String staff_type;
	private String member_name;
	
	public Staff() {}
	
	public Staff(JSONObject objJSON) {
		
		try {
			if(objJSON.has("member_id")) {
				this.member_id = objJSON.getString("member_id");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
			
			if(objJSON.has("sb_id")) {
				this.sb_id = objJSON.getString("sb_id");
			}
			
			if(objJSON.has("profile_image")) {
				JSONArray arJSON = objJSON.getJSONArray("profile_image");
				
				int size = arJSON.length();
				if(size != 0) {
					profile_image = new String[size];
					for(int i=0; i<size; i++) {
						profile_image[i] = arJSON.getString(i);
					}
				}
			}
			
			if(objJSON.has("member_email")) {
				this.member_email = objJSON.getString("member_email");
			}
			
			if(objJSON.has("chg_id")) {
				this.chg_id = objJSON.getString("chg_id");
			}
			
			if(objJSON.has("sb_nid")) {
				this.sb_nid = objJSON.getInt("sb_nid");
			}
			
			if(objJSON.has("reg_id")) {
				this.reg_id = objJSON.getString("reg_id");
			}
			
			if(objJSON.has("staff_nid")) {
				this.staff_nid = objJSON.getInt("staff_nid");
			}
			
			if(objJSON.has("url_kakao")) {
				this.url_kakao = objJSON.getString("url_kakao");
			}
			
			if(objJSON.has("profile")) {
				this.profile = objJSON.getString("profile");
			}
			
			if(objJSON.has("phone")) {
				this.phone = objJSON.getString("phone");
			}
			
			if(objJSON.has("main_image")) {
				this.main_image = objJSON.getString("main_image");
			}
			
			if(objJSON.has("url_n_home")) {
				this.url_n_home = objJSON.getString("url_n_home");
			}
			
			if(objJSON.has("url_webpage")) {
				this.url_webpage = objJSON.getString("url_webpage");
			}
			
			if(objJSON.has("url_facebook")) {
				this.url_facebook = objJSON.getString("url_facebook");
			}
			
			if(objJSON.has("url_twitter")) {
				this.url_twitter = objJSON.getString("url_twitter");
			}
			
			if(objJSON.has("url_youtube")) {
				this.url_youtube = objJSON.getString("url_youtube");
			}
			
			if(objJSON.has("url_soundCloud")) {
				this.url_soundCloud = objJSON.getString("url_soundCloud");
			}
			
			if(objJSON.has("url_instagram")) {
				this.url_instagram = objJSON.getString("url_instagram");
			}
			
			if(objJSON.has("url_kakaoStory")) {
				this.url_kakaoStory = objJSON.getString("url_kakaoStory");
			}
			
			if(objJSON.has("staff_type")) {
				this.staff_type = objJSON.getString("staff_type");
			}
			
			if(objJSON.has("member_name")) {
				this.member_name = objJSON.getString("member_name");
			}
		} catch(Exception e) {
		}
	}
	
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getSb_id() {
		return sb_id;
	}
	public void setSb_id(String sb_id) {
		this.sb_id = sb_id;
	}
	public String[] getProfile_image() {
		return profile_image;
	}
	public void setProfile_image(String[] profile_image) {
		this.profile_image = profile_image;
	}
	public String getMember_email() {
		return member_email;
	}
	public void setMember_email(String member_email) {
		this.member_email = member_email;
	}
	public String getChg_id() {
		return chg_id;
	}
	public void setChg_id(String chg_id) {
		this.chg_id = chg_id;
	}
	public int getSb_nid() {
		return sb_nid;
	}
	public void setSb_nid(int sb_nid) {
		this.sb_nid = sb_nid;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public int getStaff_nid() {
		return staff_nid;
	}
	public void setStaff_nid(int staff_nid) {
		this.staff_nid = staff_nid;
	}
	public String getUrl_kakao() {
		return url_kakao;
	}
	public void setUrl_kakao(String url_kakao) {
		this.url_kakao = url_kakao;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMain_image() {
		return main_image;
	}
	public void setMain_image(String main_image) {
		this.main_image = main_image;
	}
	public String getUrl_n_home() {
		return url_n_home;
	}
	public void setUrl_n_home(String url_n_home) {
		this.url_n_home = url_n_home;
	}
	public String getUrl_webpage() {
		return url_webpage;
	}
	public void setUrl_webpage(String url_webpage) {
		this.url_webpage = url_webpage;
	}
	public String getUrl_facebook() {
		return url_facebook;
	}
	public void setUrl_facebook(String url_facebook) {
		this.url_facebook = url_facebook;
	}
	public String getUrl_twitter() {
		return url_twitter;
	}
	public void setUrl_twitter(String url_twitter) {
		this.url_twitter = url_twitter;
	}
	public String getUrl_youtube() {
		return url_youtube;
	}
	public void setUrl_youtube(String url_youtube) {
		this.url_youtube = url_youtube;
	}
	public String getUrl_soundCloud() {
		return url_soundCloud;
	}
	public void setUrl_soundCloud(String url_soundCloud) {
		this.url_soundCloud = url_soundCloud;
	}
	public String getUrl_instagram() {
		return url_instagram;
	}
	public void setUrl_instagram(String url_instagram) {
		this.url_instagram = url_instagram;
	}
	public String getUrl_kakaoStory() {
		return url_kakaoStory;
	}
	public void setUrl_kakaoStory(String url_kakaoStory) {
		this.url_kakaoStory = url_kakaoStory;
	}
	public String getStaff_type() {
		return staff_type;
	}
	public void setStaff_type(String staff_type) {
		this.staff_type = staff_type;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
}
