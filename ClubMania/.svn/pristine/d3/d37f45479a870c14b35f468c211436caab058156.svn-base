package com.zonecomms.common.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Reply extends BaseModel {

	private int reply_nid;
	private String content;
	private Member member;
	private ArrayList<Member> target_member = new ArrayList<Member>();
	
	public Reply(){}

	public Reply(JSONObject objJSON) {
		
		super(objJSON);

		if(objJSON != null) {
			try {
				if(objJSON.has("reply_nid")) {
					this.reply_nid = objJSON.getInt("reply_nid");
					indexno = reply_nid;
				}
				
				if(objJSON.has("content")) {
					this.content = objJSON.getString("content");
				}

				if(objJSON.has("member")) {
					member = new Member(objJSON.getJSONObject("member"));
				}
				
				if(objJSON.has("target_member")) {
					try {
						JSONArray arJSON = objJSON.getJSONArray("target_member");
						
						int size = arJSON.length();
						for(int i=0; i<size; i++) {
							target_member.add(new Member(arJSON.getJSONObject(i)));
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getReply_nid() {
		return reply_nid;
	}

	public void setReply_nid(int reply_nid) {
		this.reply_nid = reply_nid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ArrayList<Member> getTarget_member() {
		
		return target_member;
	}
}
