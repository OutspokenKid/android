package com.zonecomms.common.models;

import org.json.JSONObject;

public class Message extends BaseModel {

	private int microspot_nid;
	private String post_member_nickname;
	private String content;
	private String post_media_src;
	private int content_type;
	private String mystory_member_nickname;
	private String post_member_id;
	private String mystory_member_id;
	
	public Message(){}
	
	public Message(JSONObject objJSON) {
		
		super(objJSON);
		
		try {
			if(objJSON.has("microspot_nid")) {
				microspot_nid = objJSON.getInt("microspot_nid");
				indexno = microspot_nid;
			}
			
			if(objJSON.has("content")) {
				content = objJSON.getString("content");
			}
			
			if(objJSON.has("post_member")) {
				
				JSONObject objPostMember = objJSON.getJSONObject("post_member");
				
				if(objPostMember.has("post_member_nickname")) {
					post_member_nickname = objPostMember.getString("post_member_nickname");
				}
				
				if(objPostMember.has("post_media_src")) {
					post_media_src = objPostMember.getString("post_media_src");
				}
				
				if(objPostMember.has("post_member_id")) {
					post_member_id = objPostMember.getString("post_member_id");
				}
			}
			
			if(objJSON.has("mystory_member")) {
				
				JSONObject objMyStoryMember =  objJSON.getJSONObject("mystory_member");
				
				if(objMyStoryMember.has("mystory_member_nickname")) {
					mystory_member_nickname = objMyStoryMember.getString("mystory_member_nickname");
				}
				
				if(objMyStoryMember.has("mystory_member_id")) {
					mystory_member_id = objMyStoryMember.getString("mystory_member_id");
				}
			}
			
			if(objJSON.has("content_type")) {
				content_type = objJSON.getInt("content_type");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int getMicrospot_nid() {
		return microspot_nid;
	}

	public void setMicrospot_nid(int microspot_nid) {
		this.microspot_nid = microspot_nid;
	}

	public String getPost_member_nickname() {
		return post_member_nickname;
	}

	public void setPost_member_nickname(String post_member_nickname) {
		this.post_member_nickname = post_member_nickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPost_media_src() {
		return post_media_src;
	}

	public void setPost_media_src(String post_media_src) {
		this.post_media_src = post_media_src;
	}

	public int getContent_type() {
		return content_type;
	}

	public void setContent_type(int content_type) {
		this.content_type = content_type;
	}

	public String getMystory_member_nickname() {
		return mystory_member_nickname;
	}

	public void setMystory_member_nickname(String mystory_member_nickname) {
		this.mystory_member_nickname = mystory_member_nickname;
	}

	public String getPost_member_id() {
		return post_member_id;
	}

	public void setPost_member_id(String post_member_id) {
		this.post_member_id = post_member_id;
	}

	public String getMystory_member_id() {
		return mystory_member_id;
	}

	public void setMystory_member_id(String mystory_member_id) {
		this.mystory_member_id = mystory_member_id;
	}
}
