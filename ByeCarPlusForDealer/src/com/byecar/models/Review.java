package com.byecar.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Review extends BCPBaseModel implements Serializable {

	private static final long serialVersionUID = -7244964158640641747L;
	
//	"id": "8",
//	"type": "901",
//	"dealer_id": "0",
//	"certifier_id": "1",
//	"reviewer_id": "5",
//	"onsalecar_id": "30",
//	"content": "good",
//	"rating": "5",
//	"created_at": "1419572092",
//	"priority": "1419572092",
//	"reviewer_name": "Minsang Kim",
//	"reviewer_profile_img_url": "http://byecar.minsangk.com/images/20141226/d4c9742ffa63014e718c253b72f31cc6.png",
//	"onsalecar_rep_img_url": "http://byecar.minsangk.com/images/20141219/24b472c57370af27ce37d28b5d7dda23.png",
//	"car_full_name": "현대 그랜저 XG Q25 SE",
//	"onsalecar_year": "1998",
//	"dealer_profile_img_url": null,
//	"dealer_name": null,
//	"certifier_profile_img_url": "http://byecar.minsangk.com",
//	"certifier_name": "바이카관리자"
	
	private int id;
	private int type;									//
	private int certifier_id;							//
	private int dealer_id;
	private int reviewer_id;
	private int onsalecar_id;
	private String content;
	private int rating;
	private long created_at;
	private String reviewer_name;						//
	private String reviewer_profile_img_url;
	private String onsalecar_rep_img_url;				//
	private String car_full_name;						//
	private int onsalecar_year;							//
	private String dealer_profile_img_url;				//
	private String dealer_name;							//
	private String certifier_profile_img_url;			//
	private String certifier_name;						//
	
	public Review() {
		
	}
	
	public Review(JSONObject objJSON) {
	
		super(objJSON);
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getInt("type");
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
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("reviewer_name")) {
				this.reviewer_name = objJSON.getString("reviewer_name");
			}
			
			if(objJSON.has("reviewer_profile_img_url")) {
				this.reviewer_profile_img_url = objJSON.getString("reviewer_profile_img_url");
			}
			
			if(objJSON.has("onsalecar_rep_img_url")) {
				this.onsalecar_rep_img_url = objJSON.getString("onsalecar_rep_img_url");
			}
			
			if(objJSON.has("car_full_name")) {
				this.car_full_name = objJSON.getString("car_full_name");
			}
			
			if(objJSON.has("onsalecar_year")) {
				this.onsalecar_year = objJSON.getInt("onsalecar_year");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}
			
			if(dealer_id != 0) {
				
				if(objJSON.has("dealer_profile_img_url")) {
					this.dealer_profile_img_url = objJSON.getString("dealer_profile_img_url");
				}
				
				if(objJSON.has("dealer_name")) {
					this.dealer_name = objJSON.getString("dealer_name");
				}
			}
			
			if(objJSON.has("certifier_id")) {
				this.certifier_id = objJSON.getInt("certifier_id");
			}
			
			if(certifier_id != 0) {
				
				if(objJSON.has("certifier_profile_img_url")) {
					this.certifier_profile_img_url = objJSON.getString("certifier_profile_img_url");
				}
				
				if(objJSON.has("certifier_name")) {
					this.certifier_name = objJSON.getString("certifier_name");
				}
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

	public String getReviewer_profile_img_url() {
		return reviewer_profile_img_url;
	}

	public void setReviewer_profile_img_url(String reviewer_profile_img_url) {
		this.reviewer_profile_img_url = reviewer_profile_img_url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCertifier_id() {
		return certifier_id;
	}

	public void setCertifier_id(int certifier_id) {
		this.certifier_id = certifier_id;
	}

	public String getReviewer_name() {
		return reviewer_name;
	}

	public void setReviewer_name(String reviewer_name) {
		this.reviewer_name = reviewer_name;
	}

	public String getOnsalecar_rep_img_url() {
		return onsalecar_rep_img_url;
	}

	public void setOnsalecar_rep_img_url(String onsalecar_rep_img_url) {
		this.onsalecar_rep_img_url = onsalecar_rep_img_url;
	}

	public String getCar_full_name() {
		return car_full_name;
	}

	public void setCar_full_name(String car_full_name) {
		this.car_full_name = car_full_name;
	}

	public int getOnsalecar_year() {
		return onsalecar_year;
	}

	public void setOnsalecar_year(int onsalecar_year) {
		this.onsalecar_year = onsalecar_year;
	}

	public String getDealer_profile_img_url() {
		return dealer_profile_img_url;
	}

	public void setDealer_profile_img_url(String dealer_profile_img_url) {
		this.dealer_profile_img_url = dealer_profile_img_url;
	}

	public String getDealer_name() {
		return dealer_name;
	}

	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}

	public String getCertifier_profile_img_url() {
		return certifier_profile_img_url;
	}

	public void setCertifier_profile_img_url(String certifier_profile_img_url) {
		this.certifier_profile_img_url = certifier_profile_img_url;
	}

	public String getCertifier_name() {
		return certifier_name;
	}

	public void setCertifier_name(String certifier_name) {
		this.certifier_name = certifier_name;
	}
}
