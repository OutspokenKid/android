package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cmons.cph.ShopActivity;
import com.outspoken_kid.utils.LogUtils;

public class Wholesale extends Shop implements Serializable {

	private static final long serialVersionUID = -3343791879247460050L;
	
	public static String profileImage;
	
	private int sample_available;
	private String location;
	private int customers_cnt;
	private int products_cnt;
	private String rep_image_url;
	private int[] category_ids;
	private Account[] accounts;

	// 소매 - 거래처 관리에서 쓰임.
	private boolean standingBy;

	public Wholesale(JSONObject objJSON) {
		super(objJSON);

		try {
			if (objJSON.has("sample_available")) {
				this.sample_available = objJSON.getInt("sample_available");
			}
			if (objJSON.has("location")) {
				this.location = objJSON.getString("location");
			}
			if (objJSON.has("rep_image_url")) {
				rep_image_url = objJSON.getString("rep_image_url");
			}
			
			try {
				if(getId() == ShopActivity.getInstance().user.getWholesale_id()) {
					profileImage = rep_image_url;
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}

			if (objJSON.has("customers_cnt")) {
				this.customers_cnt = objJSON.getInt("customers_cnt");
			}

			if (objJSON.has("products_cnt")) {
				this.products_cnt = objJSON.getInt("products_cnt");
			}

			if (objJSON.has("category_ids")) {

				JSONArray arJSON = objJSON.getJSONArray("category_ids");

				int size = arJSON.length();
				category_ids = new int[size];
				for (int i = 0; i < size; i++) {
					category_ids[i] = arJSON.getInt(i);
				}
			}

			if (objJSON.has("accounts")) {

				JSONArray arJSON = objJSON.getJSONArray("accounts");

				int size = arJSON.length();
				accounts = new Account[size];
				for (int i = 0; i < size; i++) {
					accounts[i] = new Account(arJSON.getJSONObject(i));
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCustomers_cnt() {
		return customers_cnt;
	}

	public void setCustomers_cnt(int customers_cnt) {
		this.customers_cnt = customers_cnt;
	}

	public int getProducts_cnt() {
		return products_cnt;
	}

	public void setProducts_cnt(int products_cnt) {
		this.products_cnt = products_cnt;
	}

	public int[] getCategory_ids() {
		return category_ids;
	}

	public void setCategory_ids(int[] category_ids) {
		this.category_ids = category_ids;
	}

	public int getSample_available() {
		return sample_available;
	}

	public void setSample_available(int sample_available) {
		this.sample_available = sample_available;
	}

	public Account[] getAccounts() {
		return accounts;
	}

	public void setAccounts(Account[] accounts) {
		this.accounts = accounts;
	}

	public boolean isStandingBy() {
		return standingBy;
	}

	public void setStandingBy(boolean standingBy) {
		this.standingBy = standingBy;
	}

	public String getRep_image_url() {
		return rep_image_url;
	}

	public void setRep_image_url(String rep_image_url) {
		this.rep_image_url = rep_image_url;
	}
}
