package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Dealer extends BaseModel {

//	"id": "1",
//	"level": "1",
//	"association": "마법사길드",
//	"complex": "개성공업단지",
//	"company": "씨몬스상사",
//	"employee_card_img_url": "http://byecar.minsangk.com/images/20141106/da2eb7df5113d10b79571",
//	"name_card_img_url": "http://byecar.minsangk.com/images/20141106/da2eb7df5113d10b79571",
//	"virtual_account_number": "",
//	"birthdate": "19850227",
//	"right_to_bid_cnt": "1",
//	"right_to_sell_cnt": "1",
//	"status": "1"
	
	private int id;
	private int level;
	private String association;
	private String complex;
	private String company;
	private String employee_card_img_url;
	private String name_card_img_url;
	private String virtual_account_number;
	private String birthdate;
	private int right_to_bid_cnt;
	private int right_to_sell_cnt;
	private int status;
	
	private String profile_img_url;
	private String name;
	private String address;
	private String phone_number;
	private String desc;
	
	public Dealer() {
		
	}
	
	public Dealer(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
				setIndexno(id);
			}
			
			if(objJSON.has("level")) {
				this.level = objJSON.getInt("level");
			}
			
			if(objJSON.has("association")) {
				this.association = objJSON.getString("association");
			}
			
			if(objJSON.has("complex")) {
				this.complex = objJSON.getString("complex");
			}
			
			if(objJSON.has("company")) {
				this.company = objJSON.getString("company");
			}
			
			if(objJSON.has("employee_card_img_url")) {
				this.employee_card_img_url = objJSON.getString("employee_card_img_url");
			}
			
			if(objJSON.has("name_card_img_url")) {
				this.name_card_img_url = objJSON.getString("name_card_img_url");
			}
			
			if(objJSON.has("virtual_account_number")) {
				this.virtual_account_number = objJSON.getString("virtual_account_number");
			}
			
			if(objJSON.has("birthdate")) {
				this.birthdate = objJSON.getString("birthdate");
			}

			if(objJSON.has("right_to_bid_cnt")) {
				this.right_to_bid_cnt = objJSON.getInt("right_to_bid_cnt");
			}
			
			if(objJSON.has("right_to_sell_cnt")) {
				this.right_to_sell_cnt = objJSON.getInt("right_to_sell_cnt");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("profile_img_url")) {
				this.profile_img_url = objJSON.getString("profile_img_url");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("address")) {
				this.address = objJSON.getString("address");
			}
			
			if(objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}
			
			if(objJSON.has("desc")) {
				this.desc = objJSON.getString("desc");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getAssociation() {
		return association;
	}
	public void setAssociation(String association) {
		this.association = association;
	}
	public String getComplex() {
		return complex;
	}
	public void setComplex(String complex) {
		this.complex = complex;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEmployee_card_img_url() {
		return employee_card_img_url;
	}
	public void setEmployee_card_img_url(String employee_card_img_url) {
		this.employee_card_img_url = employee_card_img_url;
	}
	public String getName_card_img_url() {
		return name_card_img_url;
	}
	public void setName_card_img_url(String name_card_img_url) {
		this.name_card_img_url = name_card_img_url;
	}
	public String getVirtual_account_number() {
		return virtual_account_number;
	}
	public void setVirtual_account_number(String virtual_account_number) {
		this.virtual_account_number = virtual_account_number;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public int getRight_to_bid_cnt() {
		return right_to_bid_cnt;
	}
	public void setRight_to_bid_cnt(int right_to_bid_cnt) {
		this.right_to_bid_cnt = right_to_bid_cnt;
	}
	public int getRight_to_sell_cnt() {
		return right_to_sell_cnt;
	}
	public void setRight_to_sell_cnt(int right_to_sell_cnt) {
		this.right_to_sell_cnt = right_to_sell_cnt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getProfile_img_url() {
		return profile_img_url;
	}

	public void setProfile_img_url(String profile_img_url) {
		this.profile_img_url = profile_img_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
