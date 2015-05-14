package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class CompanyInfo {

	private String phone_number;
	private String email;
	private String homepage;
	private String facebook_url;
	private String blog_url;
	
	public CompanyInfo() {
		
	}
	
	public CompanyInfo(JSONObject objJSON) {
		
		try {
			if (objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}

			if (objJSON.has("email")) {
				this.email = objJSON.getString("email");
			}

			if (objJSON.has("homepage")) {
				this.homepage = objJSON.getString("homepage");
			}

			if (objJSON.has("facebook_url")) {
				this.facebook_url = objJSON.getString("facebook_url");
			}
			
			if (objJSON.has("blog_url")) {
				this.blog_url = objJSON.getString("blog_url");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getFacebook_url() {
		return facebook_url;
	}

	public void setFacebook_url(String facebook_url) {
		this.facebook_url = facebook_url;
	}
	
	public String getBlog_url() {
		return blog_url;
	}

	public void setBlog_url(String blog_url) {
		this.blog_url = blog_url;
	}
}
