package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class OrderSet extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 4521797193725615445L;
	
	private String collapse_key;
	private int retail_id;
	private int wholesale_id;
	private int payment_type;
	private int payment_account_id;
	private String payment_purchaser_info;
	private String wholesale_name;
	private String wholesale_location;
	private String retail_name;
	private String retail_phone_number;
	private long sum;
	private int status;
	private Order[] items;
	
	private boolean isOnline;
	
	public OrderSet() {
	}
	
	public OrderSet(JSONObject objJSON) {
	
		try {
			if(objJSON.has("collapse_key")) {
				this.collapse_key = objJSON.getString("collapse_key");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("payment_type")) {
				this.payment_type = objJSON.getInt("payment_type");
			}
			
			if(objJSON.has("payment_account_id")) {
				this.payment_account_id = objJSON.getInt("payment_account_id");
			}
			
			if(objJSON.has("payment_purchaser_info")) {
				this.payment_purchaser_info = objJSON.getString("payment_purchaser_info");
			}
			
			if(objJSON.has("wholesale_name")) {
				this.wholesale_name = objJSON.getString("wholesale_name");
			}
			
			if(objJSON.has("wholesale_location")) {
				this.wholesale_location = objJSON.getString("wholesale_location");
			}
			
			if(objJSON.has("retail_name")) {
				this.retail_name = objJSON.getString("retail_name");
			}
			
			if(objJSON.has("retail_phone_number")) {
				this.retail_phone_number = objJSON.getString("retail_phone_number");
			}
			
			if(objJSON.has("sum")) {
				this.sum = objJSON.getLong("sum");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("items")) {
				
				JSONArray arJSON = objJSON.getJSONArray("items");
				int size = arJSON.length();
				items = new Order[size];
				for(int i=0; i<size; i++) {
					items[i] = new Order(arJSON.getJSONObject(i));
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getCollapse_key() {
		return collapse_key;
	}

	public void setCollapse_key(String collapse_key) {
		this.collapse_key = collapse_key;
	}

	public int getRetail_id() {
		return retail_id;
	}

	public void setRetail_id(int retail_id) {
		this.retail_id = retail_id;
	}

	public int getWholesale_id() {
		return wholesale_id;
	}

	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}

	public int getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(int payment_type) {
		this.payment_type = payment_type;
	}

	public int getPayment_account_id() {
		return payment_account_id;
	}

	public void setPayment_account_id(int payment_account_id) {
		this.payment_account_id = payment_account_id;
	}

	public String getPayment_purchaser_info() {
		return payment_purchaser_info;
	}

	public void setPayment_purchaser_info(String payment_purchaser_info) {
		this.payment_purchaser_info = payment_purchaser_info;
	}

	public String getWholesale_name() {
		return wholesale_name;
	}

	public void setWholesale_name(String wholesale_name) {
		this.wholesale_name = wholesale_name;
	}

	public String getWholesale_location() {
		return wholesale_location;
	}

	public void setWholesale_location(String wholesale_location) {
		this.wholesale_location = wholesale_location;
	}

	public String getRetail_name() {
		return retail_name;
	}

	public void setRetail_name(String retail_name) {
		this.retail_name = retail_name;
	}

	public String getRetail_phone_number() {
		return retail_phone_number;
	}

	public void setRetail_phone_number(String retail_phone_number) {
		this.retail_phone_number = retail_phone_number;
	}

	public long getSum() {
		return sum;
	}

	public void setSum(long sum) {
		this.sum = sum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Order[] getItems() {
		return items;
	}

	public void setItems(Order[] items) {
		this.items = items;
	}
}
