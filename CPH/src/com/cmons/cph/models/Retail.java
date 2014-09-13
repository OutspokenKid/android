package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Retail extends Shop implements Serializable {

	private static final long serialVersionUID = 5960692088511113388L;

	private String address;
	private String mall_url;
	private int wholesales_cnt;
	private int favorite_wholesales_cnt;
	private int favorited_products_cnt;
	private int status;

	public Retail() {
	}

	public Retail(JSONObject objJSON) {
		super(objJSON);

		try {
			if (objJSON.has("favorited_products_cnt")) {
				this.favorited_products_cnt = objJSON
						.getInt("favorited_products_cnt");
			}

			if (objJSON.has("address")) {
				this.address = objJSON.getString("address");
			}

			if (objJSON.has("mall_url")) {
				this.mall_url = objJSON.getString("mall_url");
			}

			if (objJSON.has("wholesales_cnt")) {
				this.wholesales_cnt = objJSON.getInt("wholesales_cnt");
			}

			if (objJSON.has("favorite_wholesales_cnt")) {
				this.favorite_wholesales_cnt = objJSON
						.getInt("favorite_wholesales_cnt");
			}

			if (objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}

			printModel();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void printModel() {

		LogUtils.log("###Retail.printModel.  ====================="
				+ "\naddress : " + address + "\nmall_url : " + mall_url
				+ "\nwholesales_cnt : " + wholesales_cnt
				+ "\nfavorite_wholesales_cnt : " + favorite_wholesales_cnt);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMall_url() {
		return mall_url;
	}

	public void setMall_url(String mall_url) {
		this.mall_url = mall_url;
	}

	public int getWholesales_cnt() {
		return wholesales_cnt;
	}

	public void setWholesales_cnt(int wholesales_cnt) {
		this.wholesales_cnt = wholesales_cnt;
	}

	public int getFavorite_wholesales_cnt() {
		return favorite_wholesales_cnt;
	}

	public void setFavorite_wholesales_cnt(int favorite_wholesales_cnt) {
		this.favorite_wholesales_cnt = favorite_wholesales_cnt;
	}

	public int getFavorited_products_cnt() {
		return favorited_products_cnt;
	}

	public void setFavorited_products_cnt(int favorited_products_cnt) {
		this.favorited_products_cnt = favorited_products_cnt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
