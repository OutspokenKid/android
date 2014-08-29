package com.zonecomms.common.models;

import org.json.JSONObject;

/**
 * v1.0.1
 * 
 * @author HyungGunKim
 *
 * v1.0.1 - Add facebook and kakao.
 */
public class MyStoryInfo {
	
	private String address;
	private int member_age;
	private String member_gender;
	private String mystory_member_info_open_yn;
	private String mystory_member_nid;
	private String mystory_member_id;
	private String mystory_member_nickname;
	private String mystory_title;
	private String mystory_member_email;
	private String mystory_member_profile;
	private String mystory_status_no;
	private String playground;
	private String mystory_birthday;
	private String mystory_spot_cnt;
	private String mystory_mobile_no;
	private String mystory_friend_plus_cnt;
	private String is_friend;
	private String job;
	private String jobname;
	private String ilike;
	private String mstatus_name;
	private int status;
	private String facebook;
	private String kakao;
	
	public MyStoryInfo() {}
	
	public MyStoryInfo(JSONObject objJSON) {
		
		try {
			if(objJSON.has("address")) {
				address = objJSON.getString("address");
			}
			
			if(objJSON.has("member_age")) {
				member_age = objJSON.getInt("member_age");
			}
			
			if(objJSON.has("member_gender")) {
				member_gender = objJSON.getString("member_gender");
			}
			
			if(objJSON.has("mystory_member_info_open_yn")) {
				mystory_member_info_open_yn = objJSON.getString("mystory_member_info_open_yn");
			}
			
			if(objJSON.has("mystory_member_nid")) {
				mystory_member_nid = objJSON.getString("mystory_member_nid");
			}
			
			if(objJSON.has("mystory_member_id")) {
				mystory_member_id = objJSON.getString("mystory_member_id");
			}
			
			if(objJSON.has("mystory_member_nickname")) {
				mystory_member_nickname = objJSON.getString("mystory_member_nickname");
			}
			
			if(objJSON.has("mystory_title")) {
				mystory_title = objJSON.getString("mystory_title");
			}
			
			if(objJSON.has("mystory_member_email")) {
				mystory_member_email = objJSON.getString("mystory_member_email");
			}
			
			if(objJSON.has("mystory_member_profile")) {
				mystory_member_profile = objJSON.getString("mystory_member_profile");
			}
			
			if(objJSON.has("mystory_status_no")) {
				mystory_status_no = objJSON.getString("mystory_status_no");
			}
			
			if(objJSON.has("playground")) {
				playground = objJSON.getString("playground");
			}
			
			if(objJSON.has("mystory_birthday")) {
				mystory_birthday = objJSON.getString("mystory_birthday");
			}
			
			if(objJSON.has("mystory_spot_cnt")) {
				mystory_spot_cnt = objJSON.getString("mystory_spot_cnt");
			}
			
			if(objJSON.has("mystory_mobile_no")) {
				mystory_mobile_no = objJSON.getString("mystory_mobile_no");
			}
			
			if(objJSON.has("mystory_friend_plus_cnt")) {
				mystory_friend_plus_cnt = objJSON.getString("mystory_friend_plus_cnt");
			}
			
			if(objJSON.has("is_friend")) {
				is_friend = objJSON.getString("is_friend");
			}
			
			if(objJSON.has("job")) {
				job = objJSON.getString("job");
			}
			
			if(objJSON.has("jobname")) {
				jobname = objJSON.getString("jobname");
			}
			
			if(objJSON.has("ilike")) {
				ilike = objJSON.getString("ilike");
			}
			
			if(objJSON.has("mstatus_name")) {
				mstatus_name = objJSON.getString("mstatus_name");
			}
			
			if(objJSON.has("status")) {
				status = objJSON.getInt("status");
			}
			
			if(objJSON.has("facebook")) {
				facebook = objJSON.getString("facebook");
			}
			
			if(objJSON.has("kakao")) {
				kakao = objJSON.getString("kakao");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getMember_age() {
		return member_age;
	}
	public void setMember_age(int member_age) {
		this.member_age = member_age;
	}
	public String getMember_gender() {
		return member_gender;
	}
	public void setMember_gender(String member_gender) {
		this.member_gender = member_gender;
	}
	public String getMystory_member_info_open_yn() {
		return mystory_member_info_open_yn;
	}
	public void setMystory_member_info_open_yn(String mystory_member_info_open_yn) {
		this.mystory_member_info_open_yn = mystory_member_info_open_yn;
	}
	public String getMystory_member_nid() {
		return mystory_member_nid;
	}
	public void setMystory_member_nid(String mystory_member_nid) {
		this.mystory_member_nid = mystory_member_nid;
	}
	public String getMystory_member_id() {
		return mystory_member_id;
	}
	public void setMystory_member_id(String mystory_member_id) {
		this.mystory_member_id = mystory_member_id;
	}
	public String getMystory_member_nickname() {
		return mystory_member_nickname;
	}
	public void setMystory_member_nickname(String mystory_member_nickname) {
		this.mystory_member_nickname = mystory_member_nickname;
	}
	public String getMystory_title() {
		return mystory_title;
	}
	public void setMystory_title(String mystory_title) {
		this.mystory_title = mystory_title;
	}
	public String getMystory_member_email() {
		return mystory_member_email;
	}
	public void setMystory_member_email(String mystory_member_email) {
		this.mystory_member_email = mystory_member_email;
	}
	public String getMystory_member_profile() {
		return mystory_member_profile;
	}
	public void setMystory_member_profile(String mystory_member_profile) {
		this.mystory_member_profile = mystory_member_profile;
	}
	public String getMystory_status_no() {
		return mystory_status_no;
	}
	public void setMystory_status_no(String mystory_status_no) {
		this.mystory_status_no = mystory_status_no;
	}
	public String getPlayground() {
		return playground;
	}
	public void setPlayground(String playground) {
		this.playground = playground;
	}
	public String getMystory_birthday() {
		return mystory_birthday;
	}
	public void setMystory_birthday(String mystory_birthday) {
		this.mystory_birthday = mystory_birthday;
	}
	public String getMystory_spot_cnt() {
		return mystory_spot_cnt;
	}
	public void setMystory_spot_cnt(String mystory_spot_cnt) {
		this.mystory_spot_cnt = mystory_spot_cnt;
	}
	public String getMystory_mobile_no() {
		return mystory_mobile_no;
	}
	public void setMystory_mobile_no(String mystory_mobile_no) {
		this.mystory_mobile_no = mystory_mobile_no;
	}
	public String getMystory_friend_plus_cnt() {
		return mystory_friend_plus_cnt;
	}
	public void setMystory_friend_plus_cnt(String mystory_friend_plus_cnt) {
		this.mystory_friend_plus_cnt = mystory_friend_plus_cnt;
	}
	public String getIs_friend() {
		return is_friend;
	}
	public void setIs_friend(String is_friend) {
		this.is_friend = is_friend;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getIlike() {
		return ilike;
	}
	public void setIlike(String ilike) {
		this.ilike = ilike;
	}

	public String getMstatus_name() {
		return mstatus_name;
	}

	public void setMstatus_name(String mstatus_name) {
		this.mstatus_name = mstatus_name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getKakao() {
		return kakao;
	}

	public void setKakao(String kakao) {
		this.kakao = kakao;
	}

}
