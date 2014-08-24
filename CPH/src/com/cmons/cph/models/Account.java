package com.cmons.cph.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Account extends BaseModel {

	private int id;
	private int wholesale_id;
	private String bank;
	private String depositor;
	private String number;
	
	public Account() {
	}
	
	public Account(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("bank")) {
				this.bank = objJSON.getString("bank");
			}
			
			if(objJSON.has("depositor")) {
				this.depositor = objJSON.getString("depositor");
			}
			
			if(objJSON.has("number")) {
				this.number = objJSON.getString("number");
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
	public int getWholesale_id() {
		return wholesale_id;
	}
	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getDepositor() {
		return depositor;
	}
	public void setDepositor(String depositor) {
		this.depositor = depositor;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
