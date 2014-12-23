package com.byecar.byecarplus.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Review extends BaseModel {

//	"id": "5",
//	"dealer_id": "4",
//	"reviewer_id": "5",
//	"onsalecar_id": "23",
//	"content": "그냥그랬음",
//	"rating": "2",
//	"created_at": "1419312202",
//	"reviewer_nickname": "민상kk",
//	"reviewer_profile_img_url": "abc"
	
	private int id;
	private int dealer_id;
	private int reviewer_id;
	private int onsalecar_id;
	private String content;
	private int rating;
	private long created_at;
	private String reviewer_nickname;
	private String reviewer_profile_img_url;
	private long priority;
	
	public Review() {
		
	}
	
	public Review(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}
			
			if(objJSON.has("reviewer_id")) {
				this.reviewer_id = objJSON.getInt("reviewer_id");
			}
			
			if(objJSON.has("onsalecar_id")) {
				this.onsalecar_id = objJSON.getInt("onsalecar_id");
			}
			
			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("rating")) {
				this.rating = objJSON.getInt("rating");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getInt("created_at");
			}
			
			if(objJSON.has("reviewer_nickname")) {
				this.reviewer_nickname = objJSON.getString("reviewer_nickname");
			}
			
			if(objJSON.has("reviewer_profile_img_url")) {
				this.reviewer_profile_img_url = objJSON.getString("reviewer_profile_img_url");
			}
			
			if(objJSON.has("priority")) {
				this.priority = objJSON.getLong("priority");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public int getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(int dealer_id) {
		this.dealer_id = dealer_id;
	}
	public int getOnsalecar_id() {
		return onsalecar_id;
	}
	public void setOnsalecar_id(int onsalecar_id) {
		this.onsalecar_id = onsalecar_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReviewer_id() {
		return reviewer_id;
	}

	public void setReviewer_id(int reviewer_id) {
		this.reviewer_id = reviewer_id;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getReviewer_nickname() {
		return reviewer_nickname;
	}

	public void setReviewer_nickname(String reviewer_nickname) {
		this.reviewer_nickname = reviewer_nickname;
	}

	public String getReviewer_profile_img_url() {
		return reviewer_profile_img_url;
	}

	public void setReviewer_profile_img_url(String reviewer_profile_img_url) {
		this.reviewer_profile_img_url = reviewer_profile_img_url;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}
}
