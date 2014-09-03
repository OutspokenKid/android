package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Wholesale extends Shop implements Serializable {

	private static final long serialVersionUID = -3343791879247460050L;
	
	private int id;
	private String name;
	private String phone_number;
	private String owner_id;
	private String corp_reg_number;
	private int sample_available;
	private int favorited_products_cnt;
	private int total_visited_cnt;
	private int today_visited_cnt;
	private long created_at;
	private String location;
	private String owner_name;
	private String owner_phone_number;
	private String rep_image_url;
	private int customers_cnt;
	private int favorited_cnt;
	private int products_cnt;
		
	private int[] category_ids;
	private Account[] accounts;
	
	//소매 - 거래처 관리에서 쓰임.
	private boolean standingBy;
	
	public Wholesale(JSONObject objJSON) {
		super(objJSON);
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			if(objJSON.has("phone_number")) {
				this.phone_number = objJSON.getString("phone_number");
			}
			if(objJSON.has("owner_id")) {
				this.owner_id = objJSON.getString("owner_id");
			}
			if(objJSON.has("corp_reg_number")) {
				this.corp_reg_number = objJSON.getString("corp_reg_number");
			}
			
			if(objJSON.has("sample_available")) {
				this.sample_available = objJSON.getInt("sample_available");
			}
			
			if(objJSON.has("favorited_products_cnt")) {
				this.favorited_products_cnt = objJSON.getInt("favorited_products_cnt");
			}
			
			if(objJSON.has("total_visited_cnt")) {
				this.total_visited_cnt = objJSON.getInt("total_visited_cnt");
			}
			
			if(objJSON.has("today_visited_cnt")) {
				this.today_visited_cnt = objJSON.getInt("today_visited_cnt");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("location")) {
				this.location = objJSON.getString("location");
			}
			
			if(objJSON.has("owner_name")) {
				this.owner_name = objJSON.getString("owner_name");
			}
			
			if(objJSON.has("owner_phone_number")) {
				this.owner_phone_number = objJSON.getString("owner_phone_number");
			}
			
			if(objJSON.has("rep_image_url")) {
				this.rep_image_url = objJSON.getString("rep_image_url");
			}
			
			if(objJSON.has("customers_cnt")) {
				this.customers_cnt = objJSON.getInt("customers_cnt");
			}
			
			if(objJSON.has("favorited_cnt")) {
				this.favorited_cnt = objJSON.getInt("favorited_cnt");
			}
			
			if(objJSON.has("products_cnt")) {
				this.products_cnt = objJSON.getInt("products_cnt");
			}
			
			if(objJSON.has("category_ids")) {
				
				JSONArray arJSON = objJSON.getJSONArray("category_ids");
				
				int size = arJSON.length();
				category_ids = new int[size];
				for(int i=0; i<size; i++) {
					category_ids[i] = arJSON.getInt(i);
				}
			}
			
			if(objJSON.has("accounts")) {
				
				JSONArray arJSON = objJSON.getJSONArray("accounts");
				
				int size = arJSON.length();
				accounts = new Account[size];
				for(int i=0; i<size; i++) {
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
	public String getOwner_name() {
		return owner_name;
	}
	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}
	public String getOwner_phone_number() {
		return owner_phone_number;
	}
	public void setOwner_phone_number(String owner_phone_number) {
		this.owner_phone_number = owner_phone_number;
	}
	public String getRep_image_url() {
		return rep_image_url;
	}
	public void setRep_image_url(String rep_image_url) {
		this.rep_image_url = rep_image_url;
	}
	public int getCustomers_cnt() {
		return customers_cnt;
	}
	public void setCustomers_cnt(int customers_cnt) {
		this.customers_cnt = customers_cnt;
	}
	public int getFavorited_cnt() {
		return favorited_cnt;
	}
	public void setFavorited_cnt(int favorited_cnt) {
		this.favorited_cnt = favorited_cnt;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getCorp_reg_number() {
		return corp_reg_number;
	}

	public void setCorp_reg_number(String corp_reg_number) {
		this.corp_reg_number = corp_reg_number;
	}

	public int getSample_available() {
		return sample_available;
	}

	public void setSample_available(int sample_available) {
		this.sample_available = sample_available;
	}

	public int getFavorited_products_cnt() {
		return favorited_products_cnt;
	}

	public void setFavorited_products_cnt(int favorited_products_cnt) {
		this.favorited_products_cnt = favorited_products_cnt;
	}

	public int getTotal_visited_cnt() {
		return total_visited_cnt;
	}

	public void setTotal_visited_cnt(int total_visited_cnt) {
		this.total_visited_cnt = total_visited_cnt;
	}

	public int getToday_visited_cnt() {
		return today_visited_cnt;
	}

	public void setToday_visited_cnt(int today_visited_cnt) {
		this.today_visited_cnt = today_visited_cnt;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
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
}
