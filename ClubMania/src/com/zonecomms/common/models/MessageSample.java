package com.zonecomms.common.models;

import org.json.JSONObject;

public class MessageSample extends BaseModel {

	private int member_nid;
	private String member_id;
	private String media_src;
	private String member_nickname;
	
	private String latest_msg;
	private String latest_date;
	
	private int relation_nid;
	
	private String new_check;
	
	public MessageSample(){}
	
	public MessageSample(JSONObject objJSON) {
		
		super(objJSON);
		
		try {
			if(objJSON.has("member")) {
				JSONObject objMember = objJSON.getJSONObject("member");
				
				if(objMember.has("member_nid")) {
					member_nid = objMember.getInt("member_nid");
				}
				
				if(objMember.has("member_id")) {
					member_id = objMember.getString("member_id");
				}
				
				if(objMember.has("media_src")) {
					media_src = objMember.getString("media_src");
				}
				
				if(objMember.has("member_nickname")) {
					member_nickname = objMember.getString("member_nickname");
				}
			}
			
			if(objJSON.has("latest_msg")) {
				latest_msg = objJSON.getString("latest_msg");
			}
			
			if(objJSON.has("latest_date")) {
				latest_date = objJSON.getString("latest_date");
			}
			
			if(objJSON.has("relation_nid")) {
				relation_nid = objJSON.getInt("relation_nid");
			}
			
			if(objJSON.has("new_check")) {
				new_check = objJSON.getString("new_check");
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
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMedia_src() {
		return media_src;
	}
	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}
	public String getMember_nickname() {
		return member_nickname;
	}
	public void setMember_nickname(String member_nickname) {
		this.member_nickname = member_nickname;
	}
	public String getLatest_msg() {
		return latest_msg;
	}
	public void setLatest_msg(String latest_msg) {
		this.latest_msg = latest_msg;
	}
	public String getLatest_date() {
		return latest_date;
	}
	public void setLatest_date(String latest_date) {
		this.latest_date = latest_date;
	}

	public int getRelation_nid() {
		return relation_nid;
	}

	public void setRelation_nid(int relation_nid) {
		this.relation_nid = relation_nid;
	}

	public String getNew_check() {
		return new_check;
	}

	public void setNew_check(String new_check) {
		this.new_check = new_check;
	}
}
